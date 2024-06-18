package com.example.cookingrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.*


class ShoppingListFragment : Fragment() {

    private val shoppingListItemViewModel: ShoppingListItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.shopping_list_recyclerview)
        val adapter = ShoppingListAdapter(
            onDeleteClick = { item ->
                shoppingListItemViewModel.delete(item)
                Toast.makeText(requireContext(), getString(R.string.after_delete_item_alert), Toast.LENGTH_SHORT).show()

            },
            onMinusClick = { item ->
                shoppingListItemViewModel.decrementQuantity(item)
            },
            onPlusClick = { item ->
                shoppingListItemViewModel.incrementQuantity(item)
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        shoppingListItemViewModel.allItems.observe(viewLifecycleOwner) { items ->
            items?.let { adapter.submitList(it) }
        }

        val nameEditText: EditText = view.findViewById(R.id.edit_ingredient_name)
        val quantityEditText: EditText = view.findViewById(R.id.edit_ingredient_quantity)
        val addButton: Button = view.findViewById(R.id.button_add_ingredient)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val quantity = quantityEditText.text.toString()
            if (name.isNotEmpty() && quantity.isNotEmpty()) {
                val item = ShoppingListItem(name = name, quantity = quantity)
                shoppingListItemViewModel.insertOrUpdate(item)
                nameEditText.text.clear()
                quantityEditText.text.clear()
                Toast.makeText(requireContext(), getString(R.string.item_saved_alert), Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(requireContext(), getString(R.string.please_fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
