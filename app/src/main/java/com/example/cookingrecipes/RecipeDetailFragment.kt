package com.example.cookingrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class RecipeDetailFragment : Fragment() {

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val shoppingListItemViewModel: ShoppingListItemViewModel by viewModels()
    private var recipeId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        val recipeNameTextView = view.findViewById<TextView>(R.id.recipe_name)
        val ingredientsList = view.findViewById<LinearLayout>(R.id.ingredients_list)
        val recipeInstructionsTextView = view.findViewById<TextView>(R.id.recipe_instructions)
        val editButton = view.findViewById<Button>(R.id.button_edit_recipe)

        recipeId = arguments?.getInt("recipeId") ?: -1
        if (recipeId != -1) {
            // Fetch and display the recipe details using recipeViewModel
            recipeViewModel.getRecipeById(recipeId).observe(viewLifecycleOwner) { recipe ->
                recipe?.let {
                    recipeNameTextView.text = it.name
                    recipeInstructionsTextView.text = it.instructions

                    // Display ingredients with add to shopping list button
                    ingredientsList.removeAllViews()
                    it.ingredients.forEach { ingredient ->
                        val ingredientView = layoutInflater.inflate(R.layout.item_ingredient, null)
                        val ingredientNameTextView = ingredientView.findViewById<TextView>(R.id.ingredient_name)
                        val ingredientQuantityTextView = ingredientView.findViewById<TextView>(R.id.ingredient_quantity)
                        val addButton = ingredientView.findViewById<Button>(R.id.button_add_to_shopping_list)

                        ingredientNameTextView.text = ingredient.name
                        ingredientQuantityTextView.text = ingredient.quantity
                        addButton.setOnClickListener {
                            val shoppingListItem = ShoppingListItem(name = ingredient.name, quantity = ingredient.quantity)
                            shoppingListItemViewModel.insert(shoppingListItem)
                        }

                        ingredientsList.addView(ingredientView)
                    }
                }
            }
        }

        editButton.setOnClickListener {
            val actionId = R.id.action_recipeDetailFragment_to_editRecipeFragment
            val bundle = Bundle().apply { putInt("recipeId", recipeId) }
            findNavController().navigate(actionId, bundle)
        }

        return view
    }
}
