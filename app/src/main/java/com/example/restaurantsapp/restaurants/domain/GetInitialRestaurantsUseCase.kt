package com.example.restaurantsapp.restaurants.domain

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import jakarta.inject.Inject

class GetInitialRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantsRepository
) {
    suspend operator fun invoke() {
        repository.loadRestaurants()
    }
}