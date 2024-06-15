package com.example.cookingrecipes

import androidx.lifecycle.LiveData

class ShoppingListItemRepository(private val shoppingListItemDao: ShoppingListItemDao) {

    val allItems: LiveData<List<ShoppingListItem>> = shoppingListItemDao.getAllItems()

    suspend fun insertOrUpdate(item: ShoppingListItem) {
        val existingItem = shoppingListItemDao.getItemByName(item.name)
        if (existingItem != null) {
            val newQuantity = (existingItem.quantity.toInt() + item.quantity.toInt()).toString()
            val updatedItem = existingItem.copy(quantity = newQuantity)
            shoppingListItemDao.update(updatedItem)
        } else {
            shoppingListItemDao.insert(item)
        }
    }

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
