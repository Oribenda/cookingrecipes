package com.example.cookingrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class AddRecipeFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val ingredientViews = mutableListOf<View>()

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

        addButton.setOnClickListener {
            addIngredientField(ingredientsContainer)
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

    private fun addIngredientField(container: LinearLayout) {
        val ingredientView = layoutInflater.inflate(R.layout.item_add_ingredient, container, false)
        container.addView(ingredientView)
        ingredientViews.add(ingredientView)
    }
}
