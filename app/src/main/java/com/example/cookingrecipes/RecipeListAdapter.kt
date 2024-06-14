package com.example.cookingrecipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RecipeListAdapter(private val onItemClicked: (Recipe) -> Unit) : ListAdapter<Recipe, RecipeListAdapter.RecipeViewHolder>(RECIPES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class RecipeViewHolder(itemView: View, val onItemClicked: (Recipe) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val recipeItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(recipe: Recipe) {
            recipeItemView.text = recipe.name
            itemView.setOnClickListener {
                onItemClicked(recipe)
            }
        }
    }

    companion object {
        private val RECIPES_COMPARATOR = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }
        }
    }
}
