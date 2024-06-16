package com.example.cookingrecipes

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class AddRecipeFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val ingredientViews = mutableListOf<View>()
    private var recipeId: Int = -1
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestStoragePermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        requestStoragePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(requireContext(), "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(bitmap)
                // Save or use the bitmap as needed
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                imageView.setImageURI(imageUri)
                // Save or use the imageUri as needed
            }
        }

        // Handle the back button press
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_addRecipeFragment_to_recipeListFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        val nameEditText = view.findViewById<EditText>(R.id.edit_text_name)
        val ingredientsContainer = view.findViewById<LinearLayout>(R.id.ingredients_container)
        val instructionsEditText = view.findViewById<EditText>(R.id.edit_text_instructions)
        val addButton = view.findViewById<Button>(R.id.button_add_ingredient)
        val saveButton = view.findViewById<Button>(R.id.button_save)
        imageView = view.findViewById(R.id.recipe_image)
        val uploadButton = view.findViewById<Button>(R.id.button_upload_image)

        uploadButton.setOnClickListener {
            showImagePickerDialog()
        }

        addButton.setOnClickListener {
            addIngredientField(ingredientsContainer)
        }

        recipeId = arguments?.getInt("recipeId") ?: -1
        if (recipeId != -1) {
            recipeViewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
                recipe?.let {
                    nameEditText.setText(it.name)
                    instructionsEditText.setText(it.instructions)
                    it.ingredients.forEach { ingredient ->
                        val ingredientView = addIngredientField(ingredientsContainer)
                        ingredientView.findViewById<EditText>(R.id.edit_ingredient_name).setText(ingredient.name)
                        ingredientView.findViewById<EditText>(R.id.edit_ingredient_quantity).setText(ingredient.quantity)
                    }
                    // Load the image if it exists
                    it.imageUri?.let { uri ->
                        imageView.setImageURI(Uri.parse(uri))
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val instructions = instructionsEditText.text.toString()
            val ingredients = ingredientViews.map { ingredientView ->
                val ingredientName = ingredientView.findViewById<EditText>(R.id.edit_ingredient_name).text.toString()
                val ingredientQuantity = ingredientView.findViewById<EditText>(R.id.edit_ingredient_quantity).text.toString()
                Ingredient(ingredientName, ingredientQuantity)
            }

            if (name.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                val recipe = Recipe(
                    id = if (recipeId != -1) recipeId else 0,
                    name = name,
                    ingredients = ingredients,
                    instructions = instructions,
                    imageUri = imageUri?.toString() // Save image URI as string
                )
                if (recipeId != -1) {
                    recipeViewModel.update(recipe)
                } else {
                    recipeViewModel.insert(recipe)
                }

                Toast.makeText(requireContext(), "Recipe saved", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addRecipeFragment_to_recipeListFragment)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> checkCameraPermission()
                1 -> checkStoragePermission()
            }
        }
        builder.show()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(requireContext(), "Camera permission is needed to take a photo", Toast.LENGTH_SHORT).show()
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun checkStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(requireContext(), "Storage permission is needed to pick a photo", Toast.LENGTH_SHORT).show()
                requestStoragePermissionLauncher.launch(permission)
            }
            else -> {
                requestStoragePermissionLauncher.launch(permission)
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }

    private fun addIngredientField(container: LinearLayout): View {
        val ingredientView = layoutInflater.inflate(R.layout.item_add_ingredient, container, false)
        container.addView(ingredientView)
        ingredientViews.add(ingredientView)
        return ingredientView
    }
}
