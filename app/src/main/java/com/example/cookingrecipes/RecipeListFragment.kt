package com.example.cookingrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecipeListFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        // RecyclerView setup
        val recyclerView = view.findViewById<RecyclerView>(R.id.recipe_list_recyclerview)
        val adapter = RecipeListAdapter { recipe ->
            val actionId = R.id.action_recipeListFragment_to_recipeDetailFragment
            val bundle = Bundle().apply { putInt("recipeId", recipe.id) }
            findNavController().navigate(actionId, bundle)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observe LiveData
        recipeViewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let { adapter.submitList(it) }
        }

        // Setup FAB to navigate to AddRecipeFragment
        view.findViewById<FloatingActionButton>(R.id.fab_add_recipe).setOnClickListener {
            findNavController().navigate(R.id.action_recipeListFragment_to_addRecipeFragment)
        }
        return view
    }
}
