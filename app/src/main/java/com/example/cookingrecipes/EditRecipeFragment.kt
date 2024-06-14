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

class EditRecipeFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val ingredientViews = mutableListOf<View>()
    private var recipeId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_recipe, container, false)

        val nameEditText = view.findViewById<EditText>(R.id.edit_text_name)
        val ingredientsContainer = view.findViewById<LinearLayout>(R.id.ingredients_container)
        val instructionsEditText = view.findViewById<EditText>(R.id.edit_text_instructions)
        val addButton = view.findViewById<Button>(R.id.button_add_ingredient)
        val saveButton = view.findViewById<Button>(R.id.button_save)

        recipeId = arguments?.getInt("recipeId") ?: -1
        if (recipeId != -1) {
            recipeViewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
                recipe?.let {
                    nameEditText.setText(it.name)
                    instructionsEditText.setText(it.instructions)

                    // Populate existing ingredients
                    ingredientsContainer.removeAllViews()
                    ingredientViews.clear()
                    it.ingredients.forEach { ingredient ->
                        addIngredientField(ingredientsContainer, ingredient)
                    }
                }
            }
        }

        addButton.setOnClickListener {
            addIngredientField(ingredientsContainer, Ingredient("", ""))
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

    private fun addIngredientField(container: LinearLayout, ingredient: Ingredient) {
        val ingredientView = layoutInflater.inflate(R.layout.item_add_ingredient, container, false)
        val ingredientNameEditText = ingredientView.findViewById<EditText>(R.id.edit_ingredient_name)
        val ingredientQuantityEditText = ingredientView.findViewById<EditText>(R.id.edit_ingredient_quantity)

        ingredientNameEditText.setText(ingredient.name)
        ingredientQuantityEditText.setText(ingredient.quantity)

        container.addView(ingredientView)
        ingredientViews.add(ingredientView)
    }
}
