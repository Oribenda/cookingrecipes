package com.example.cookingrecipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recipesButton = view.findViewById<Button>(R.id.button_recipes)
        val shoppingListButton = view.findViewById<Button>(R.id.button_shopping_list)

        recipesButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_recipeListFragment)
        }

        shoppingListButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_shoppingListFragment)
        }

        return view
    }
}
