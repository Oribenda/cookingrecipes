package com.example.cookingrecipes

import android.util.Log
import androidx.lifecycle.LiveData

class ShoppingListItemRepository(private val shoppingListItemDao: ShoppingListItemDao) {
    val allItems: LiveData<List<ShoppingListItem>> = shoppingListItemDao.getAllItems()

    suspend fun insert(item: ShoppingListItem) {
        val id = shoppingListItemDao.insert(item)
        Log.d("ShoppingListItemRepo", "Inserted item with ID: $id")
    }

    suspend fun update(item: ShoppingListItem) {
        shoppingListItemDao.update(item)
        Log.d("ShoppingListItemRepo", "Updated item: $item")
    }

    suspend fun delete(item: ShoppingListItem) {
        shoppingListItemDao.delete(item)
        Log.d("ShoppingListItemRepo", "Deleted item: $item")
    }
}
