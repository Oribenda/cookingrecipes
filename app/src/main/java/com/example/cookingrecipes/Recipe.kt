package com.example.cookingrecipes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val ingredients: List<Ingredient>,
    val instructions: String,
    val imageUri: String?
)
