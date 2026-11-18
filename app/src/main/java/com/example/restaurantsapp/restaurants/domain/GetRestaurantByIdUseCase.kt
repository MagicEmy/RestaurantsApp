package com.example.restaurantsapp.restaurants.domain

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import jakarta.inject.Inject

class GetRestaurantByIdUseCase @Inject constructor(
    private val repository: RestaurantsRepository
) {
    suspend operator fun invoke(id: Int): Restaurant? {
        return repository.getRestaurantById(id)
    }
}