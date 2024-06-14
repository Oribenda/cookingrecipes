package com.example.cookingrecipes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class, ShoppingListItem::class], version = 3, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun shoppingListItemDao(): ShoppingListItemDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
