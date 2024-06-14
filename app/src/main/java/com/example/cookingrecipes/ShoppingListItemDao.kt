package com.example.cookingrecipes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingListItemDao {
    @Query("SELECT * FROM shopping_list_items")
    fun getAllItems(): LiveData<List<ShoppingListItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShoppingListItem)

    @Update
    suspend fun update(item: ShoppingListItem)

    @Delete
    suspend fun delete(item: ShoppingListItem)
}
