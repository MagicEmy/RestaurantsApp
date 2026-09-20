package com.example.restaurantsapp

data class RestaurantDetailsScreenState(
    val restaurant: Restaurant? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)