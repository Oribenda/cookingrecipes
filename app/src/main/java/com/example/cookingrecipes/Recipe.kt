package com.example.cookingrecipes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val ingredients: String,
    val instructions: String,
    val imageUrl: String? = null
)
