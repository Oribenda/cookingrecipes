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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_recipe, container, false)

        val nameEditText = view.findViewById<EditText>(R.id.edit_text_name)
        val ingredientsEditText = view.findViewById<EditText>(R.id.edit_text_ingredients)
        val instructionsEditText = view.findViewById<EditText>(R.id.edit_text_instructions)
        val saveButton = view.findViewById<Button>(R.id.button_save)

        recipeId = arguments?.getInt("recipeId") ?: -1
        if (recipeId != -1) {
            recipeViewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
                recipe?.let {
                    nameEditText.setText(it.name)
                    ingredientsEditText.setText(it.ingredients)
                    instructionsEditText.setText(it.instructions)
                }
            }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val ingredients = ingredientsEditText.text.toString()
            val instructions = instructionsEditText.text.toString()

            if (name.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                val updatedRecipe = Recipe(
                    id = recipeId,
                    name = name,
                    ingredients = ingredients,
                    instructions = instructions
                )
                recipeViewModel.update(updatedRecipe)
                findNavController().navigate(R.id.action_editRecipeFragment_to_recipeListFragment)
            }
        }

        return view
    }
}
