package com.example.cookingrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.*

class RecipeListFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle the back button press
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_recipeListFragment_to_homeFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recipe_list_recyclerview)
        val adapter = RecipeListAdapter(
            onRecipeClick = { recipe ->
                val actionId = R.id.action_recipeListFragment_to_recipeDetailFragment
                val bundle = Bundle().apply { putInt("recipeId", recipe.id) }
                findNavController().navigate(actionId, bundle)
            },
            onDeleteClick = { recipe ->
                showDeleteConfirmationDialog(recipe)
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        recipeViewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let { adapter.submitList(it) }
        }

        val addRecipeButton = view.findViewById<Button>(R.id.button_add_recipe)
        addRecipeButton.setOnClickListener {
            findNavController().navigate(R.id.action_recipeListFragment_to_addRecipeFragment)
        }

        return view
    }

    private fun showDeleteConfirmationDialog(recipe: Recipe) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete this recipe?")
            .setPositiveButton("Delete") { dialog, which ->
                recipeViewModel.delete(recipe)
                Toast.makeText(requireContext(), getString(R.string.after_delete_recipe_alert), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
