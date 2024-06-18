package com.example.cookingrecipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ShoppingListAdapter(
    private val onDeleteClick: (ShoppingListItem) -> Unit,
    private val onMinusClick: (ShoppingListItem) -> Unit,
    private val onPlusClick: (ShoppingListItem) -> Unit
) : ListAdapter<ShoppingListItem, ShoppingListAdapter.ShoppingListViewHolder>(ShoppingListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onDeleteClick, onMinusClick, onPlusClick)
    }

    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.ingredient_name)
        private val quantityTextView: TextView = itemView.findViewById(R.id.ingredient_quantity)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)
        private val minusButton: ImageButton = itemView.findViewById(R.id.button_minus)
        private val plusButton: ImageButton = itemView.findViewById(R.id.button_plus)

        fun bind(
            item: ShoppingListItem,
            onDeleteClick: (ShoppingListItem) -> Unit,
            onMinusClick: (ShoppingListItem) -> Unit,
            onPlusClick: (ShoppingListItem) -> Unit
        ) {
            nameTextView.text = item.name
            quantityTextView.text = item.quantity
            deleteButton.setOnClickListener { onDeleteClick(item) }
            minusButton.setOnClickListener { onMinusClick(item) }
            plusButton.setOnClickListener { onPlusClick(item) }
        }
    }
}

class ShoppingListDiffCallback : DiffUtil.ItemCallback<ShoppingListItem>() {
    override fun areItemsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
        return oldItem == newItem
    }
}
