package com.example.cookingrecipes

import androidx.lifecycle.LiveData

class ShoppingListItemRepository(private val shoppingListItemDao: ShoppingListItemDao) {
    val allItems: LiveData<List<ShoppingListItem>> = shoppingListItemDao.getAllItems()

    suspend fun insert(item: ShoppingListItem) {
        shoppingListItemDao.insert(item)
    }

    suspend fun update(item: ShoppingListItem) {
        shoppingListItemDao.update(item)
    }

    suspend fun delete(item: ShoppingListItem) {
        shoppingListItemDao.delete(item)
    }
}
