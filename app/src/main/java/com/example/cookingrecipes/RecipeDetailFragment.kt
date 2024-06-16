package com.example.cookingrecipes

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class RecipeDetailFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val shoppingListItemViewModel: ShoppingListItemViewModel by viewModels()
    private var recipeId: Int = -1
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the back button press
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_recipeDetailFragment_to_recipeListFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        val recipeNameTextView: TextView = view.findViewById(R.id.recipe_name)
        val ingredientsList: LinearLayout = view.findViewById(R.id.ingredients_list)
        val recipeInstructionsTextView: TextView = view.findViewById(R.id.recipe_instructions)
        val editButton: Button = view.findViewById(R.id.button_edit_recipe)
        imageView = view.findViewById(R.id.recipe_image)

        recipeId = arguments?.getInt("recipeId") ?: -1
        if (recipeId != -1) {
            recipeViewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
                recipe?.let {
                    recipeNameTextView.text = it.name
                    recipeInstructionsTextView.text = it.instructions

                    ingredientsList.removeAllViews()
                    it.ingredients.forEach { ingredient ->
                        val ingredientView = layoutInflater.inflate(R.layout.item_ingredient, null)
                        val ingredientNameTextView = ingredientView.findViewById<TextView>(R.id.ingredient_name)
                        val ingredientQuantityTextView = ingredientView.findViewById<TextView>(R.id.ingredient_quantity)
                        val addButton = ingredientView.findViewById<Button>(R.id.button_add_to_shopping_list)

                        ingredientNameTextView.text = ingredient.name
                        ingredientQuantityTextView.text = ingredient.quantity
                        addButton.setOnClickListener {
                            val item = ShoppingListItem(name = ingredient.name, quantity = ingredient.quantity)
                            shoppingListItemViewModel.insertOrUpdate(item)
                        }

                        ingredientsList.addView(ingredientView)
                    }

                    // Load the image if it exists
                    it.imageUri?.let { uri ->
                        imageView.setImageURI(Uri.parse(uri))
                    }
                }
            }
        }

        editButton.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("recipeId", recipeId)
            }
            findNavController().navigate(R.id.action_recipeDetailFragment_to_addRecipeFragment, bundle)
        }

        return view
    }
}
