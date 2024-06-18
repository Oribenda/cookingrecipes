package com.example.cookingrecipes

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var viewModel: ShoppingListItemViewModel
    private lateinit var ingredientName: TextView
    private lateinit var ingredientQuantity: TextView
    private lateinit var buttonDelete: Button
    private lateinit var buttonMinus: ImageButton
    private lateinit var buttonPlus: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_shopping_list)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(ShoppingListItemViewModel::class.java)

        // Find views by ID
        ingredientName = findViewById(R.id.ingredient_name)
        ingredientQuantity = findViewById(R.id.ingredient_quantity)
        buttonDelete = findViewById(R.id.button_delete)
        buttonMinus = findViewById(R.id.button_minus)
        buttonPlus = findViewById(R.id.button_plus)

        // Assuming you have an item to work with
        val item = ShoppingListItem(id = 1, name = "Example Ingredient", quantity = "1")

        // Set initial values
        ingredientName.text = item.name
        ingredientQuantity.text = item.quantity

        // Set click listeners
        buttonDelete.setOnClickListener {
            viewModel.delete(item)
        }

        buttonMinus.setOnClickListener {
            viewModel.decrementQuantity(item)
        }

        buttonPlus.setOnClickListener {
            viewModel.incrementQuantity(item)
        }

        // Observe changes in the quantity if it is updated elsewhere
        viewModel.allItems.observe(this, Observer { items ->
            val updatedItem = items.find { it.id == item.id }
            updatedItem?.let {
                ingredientQuantity.text = it.quantity
            }
        })
    }
}
