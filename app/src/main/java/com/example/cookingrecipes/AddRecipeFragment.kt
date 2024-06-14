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

class AddRecipeFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        val nameEditText = view.findViewById<EditText>(R.id.edit_text_name)
        val ingredientsEditText = view.findViewById<EditText>(R.id.edit_text_ingredients)
        val instructionsEditText = view.findViewById<EditText>(R.id.edit_text_instructions)
        val saveButton = view.findViewById<Button>(R.id.button_save)

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val ingredients = ingredientsEditText.text.toString()
            val instructions = instructionsEditText.text.toString()

            if (name.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                val newRecipe = Recipe(
                    name = name,
                    ingredients = ingredients,
                    instructions = instructions
                )
                recipeViewModel.insert(newRecipe)
                findNavController().navigate(R.id.action_addRecipeFragment_to_recipeListFragment)
            }
        }

        return view
    }
}
