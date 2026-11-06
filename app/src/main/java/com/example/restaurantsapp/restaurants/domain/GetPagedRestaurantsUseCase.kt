package com.example.restaurantsapp.restaurants.domain

import androidx.paging.PagingData
import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPagedRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantsRepository
) {
    operator fun invoke(): Flow<PagingData<Restaurant>> {
       return repository.getPagedRestaurants()
    }
}