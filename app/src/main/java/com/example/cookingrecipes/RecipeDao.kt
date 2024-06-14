package com.example.cookingrecipes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: Int): LiveData<Recipe>
}
