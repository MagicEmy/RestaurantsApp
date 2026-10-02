package com.example.restaurantsapp.restaurants.presentation.details

import com.example.restaurantsapp.restaurants.domain.Restaurant

data class RestaurantDetailsScreenState(
    val restaurant: Restaurant? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)