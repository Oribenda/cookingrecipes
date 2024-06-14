package com.example.cookingrecipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShoppingListFragment : Fragment() {

    private val shoppingListItemViewModel: ShoppingListItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.shopping_list_recyclerview)
        val adapter = ShoppingListAdapter { item ->
            Log.d("ShoppingListFragment", "Deleting items with name: ${item.name}")
            shoppingListItemViewModel.delete(item)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        shoppingListItemViewModel.mergedItems.observe(viewLifecycleOwner) { items ->
            Log.d("ShoppingListFragment", "Observed merged items: $items")
            items?.let { adapter.submitList(it) }
        }

        return view
    }
}
