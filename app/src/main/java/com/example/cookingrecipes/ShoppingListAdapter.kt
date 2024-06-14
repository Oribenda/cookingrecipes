package com.example.cookingrecipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ShoppingListAdapter : ListAdapter<ShoppingListItem, ShoppingListAdapter.ShoppingListViewHolder>(SHOPPING_LIST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameView: TextView = itemView.findViewById(R.id.item_name)
        private val itemQuantityView: TextView = itemView.findViewById(R.id.item_quantity)

        fun bind(item: ShoppingListItem) {
            itemNameView.text = item.name
            itemQuantityView.text = item.quantity
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
