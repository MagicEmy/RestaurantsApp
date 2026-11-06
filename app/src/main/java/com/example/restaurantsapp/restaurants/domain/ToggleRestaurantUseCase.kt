package com.example.restaurantsapp.restaurants.domain

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import jakarta.inject.Inject

class ToggleRestaurantUseCase @Inject constructor (
    private val repository: RestaurantsRepository,
){

    suspend operator fun invoke(
        id: Int,
        oldValue: Boolean
    ) {
        repository.toggleFavoriteRestaurant(
            id = id,
            value = oldValue.not()
        )
    }
}