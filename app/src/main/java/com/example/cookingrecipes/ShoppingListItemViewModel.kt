package com.example.cookingrecipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ShoppingListItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ShoppingListItemRepository
    val allItems: LiveData<List<ShoppingListItem>>

    init {
        val shoppingListItemDao = RecipeDatabase.getDatabase(application).shoppingListItemDao()
        repository = ShoppingListItemRepository(shoppingListItemDao)
        allItems = repository.allItems
    }

    fun insertOrUpdate(item: ShoppingListItem) = viewModelScope.launch {
        repository.insertOrUpdate(item)
    }

    fun insert(item: ShoppingListItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: ShoppingListItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: ShoppingListItem) = viewModelScope.launch {
        repository.delete(item)
    }

    fun incrementQuantity(item: ShoppingListItem) = viewModelScope.launch {
        val newQuantity = (item.quantity.toInt() + 1).toString()
        val updatedItem = item.copy(quantity = newQuantity)
        repository.update(updatedItem)
    }

    fun decrementQuantity(item: ShoppingListItem) = viewModelScope.launch {
        val newQuantity = (item.quantity.toInt() - 1)
        if (newQuantity > 0) {
            val updatedItem = item.copy(quantity = newQuantity.toString())
            repository.update(updatedItem)
        } else {
            repository.delete(item)
        }
    }
}
