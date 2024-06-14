package com.example.cookingrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class EditRecipeFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()
    private var recipeId: Int = -1

    private lateinit var recipeNameEditText: EditText
    private lateinit var recipeInstructionsEditText: EditText
    private lateinit var recipeIngredientsEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_recipe, container, false)

        recipeNameEditText = view.findViewById(R.id.edit_recipe_name)
        recipeInstructionsEditText = view.findViewById(R.id.edit_recipe_instructions)
        recipeIngredientsEditText = view.findViewById(R.id.edit_recipe_ingredients)
        val saveButton = view.findViewById<Button>(R.id.button_save_recipe)

        recipeId = arguments?.getInt("recipeId") ?: -1
        if (recipeId != -1) {
            recipeViewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
                recipe?.let {
                    recipeNameEditText.setText(it.name)
                    recipeInstructionsEditText.setText(it.instructions)
                    recipeIngredientsEditText.setText(it.ingredients.joinToString { ingredient -> "${ingredient.name}:${ingredient.quantity}" })
                }
            }
        }

        saveButton.setOnClickListener {
            val ingredients = recipeIngredientsEditText.text.toString().split(",").map {
                val parts = it.split(":")
                Ingredient(parts[0], parts[1])
            }
            val updatedRecipe = Recipe(
                id = recipeId,
                name = recipeNameEditText.text.toString(),
                instructions = recipeInstructionsEditText.text.toString(),
                ingredients = ingredients
            )
            recipeViewModel.update(updatedRecipe)
            findNavController().navigate(R.id.action_editRecipeFragment_to_recipeListFragment)
        }

        return view
    }
}
