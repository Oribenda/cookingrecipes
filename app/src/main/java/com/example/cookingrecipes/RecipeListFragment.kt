package com.example.cookingrecipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
                recipeViewModel.delete(recipe)
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
}
