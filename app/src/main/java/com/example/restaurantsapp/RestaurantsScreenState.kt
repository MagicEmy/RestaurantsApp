package com.example.restaurantsapp

data class RestaurantsScreenState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)