package com.example.restaurantsapp

import com.example.restaurantsapp.restaurants.data.remote.RemoteRestaurant
import com.example.restaurantsapp.restaurants.data.remote.RestaurantsApiService
import com.example.restaurantsapp.restaurants.presentation.DummyContent
import kotlinx.coroutines.delay

class FakeApiService( private val shouldThrowException: Boolean = false
) : RestaurantsApiService {
    override suspend fun getRestaurants()
    :List<RemoteRestaurant> {
        delay(1000)
        if (shouldThrowException) {
            throw Exception("Something went wrong.")
        }
        return DummyContent.getRemoteRestaurants()
    }
    override suspend fun getRestaurant(id: Int)
            : Map<String, RemoteRestaurant> {
        return DummyContent.getRemoteRestaurants().associateBy { it.id.toString() }
    }
}