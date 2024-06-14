package com.example.cookingrecipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

class ShoppingListAdapter(private val onDeleteClick: (ShoppingListItem) -> Unit) : ListAdapter<ShoppingListItem, ShoppingListAdapter.ShoppingListViewHolder>(SHOPPING_LIST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListViewHolder(view, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ShoppingListViewHolder(itemView: View, private val onDeleteClick: (ShoppingListItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val itemNameView: TextView = itemView.findViewById(R.id.item_name)
        private val itemQuantityView: TextView = itemView.findViewById(R.id.item_quantity)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)

        fun bind(item: ShoppingListItem) {
            itemNameView.text = item.name
            itemQuantityView.text = item.quantity
            deleteButton.setOnClickListener {
                Log.d("ShoppingListAdapter", "Delete button clicked for item: $item")
                onDeleteClick(item)
            }
        }
    }

    companion object {
        private val SHOPPING_LIST_COMPARATOR = object : DiffUtil.ItemCallback<ShoppingListItem>() {
            override fun areItemsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
