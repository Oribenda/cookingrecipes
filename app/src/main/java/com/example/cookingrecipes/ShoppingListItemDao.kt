package com.example.cookingrecipes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingListItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShoppingListItem)

    @Update
    suspend fun update(item: ShoppingListItem)

    @Delete
    suspend fun delete(item: ShoppingListItem)

    @Query("SELECT * FROM shopping_list_items ORDER BY name ASC")
    fun getAllItems(): LiveData<List<ShoppingListItem>>

    @Query("SELECT * FROM shopping_list_items WHERE name = :name LIMIT 1")
    suspend fun getItemByName(name: String): ShoppingListItem?
}
