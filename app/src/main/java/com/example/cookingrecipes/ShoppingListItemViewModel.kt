package com.example.cookingrecipes

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ShoppingListItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ShoppingListItemRepository
    private val _mergedItems = MutableLiveData<List<ShoppingListItem>>()
    val mergedItems: LiveData<List<ShoppingListItem>> get() = _mergedItems

    init {
        val shoppingListItemDao = RecipeDatabase.getDatabase(application).shoppingListItemDao()
        repository = ShoppingListItemRepository(shoppingListItemDao)

        repository.allItems.observeForever { items ->
            _mergedItems.value = mergeItems(items)
            Log.d("ShoppingListItemVM", "Merged Items: ${_mergedItems.value}")
        }
    }

    private fun mergeItems(items: List<ShoppingListItem>): List<ShoppingListItem> {
        val mergedMap = mutableMapOf<String, ShoppingListItem>()
        items.forEach { item ->
            if (mergedMap.containsKey(item.name)) {
                val existingItem = mergedMap[item.name]!!
                val newQuantity = existingItem.quantity.toInt() + item.quantity.toInt()
                mergedMap[item.name] = existingItem.copy(quantity = newQuantity.toString())
            } else {
                mergedMap[item.name] = item
            }
        }
        return mergedMap.values.toList()
    }

    fun insert(item: ShoppingListItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: ShoppingListItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: ShoppingListItem) = viewModelScope.launch {
        Log.d("ShoppingListItemVM", "Deleting items with name: ${item.name}")
        repository.deleteItemsByName(item.name)
    }
}
