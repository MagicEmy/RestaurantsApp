package com.example.restaurantsapp

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class RestaurantsRepositoryTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun toggleFavorite_updatesFavoriteState() = scope.runTest {
        val repository = RestaurantsRepository(
            FakeApiService(),
            FakeRoomDao(),
            dispatcher
        )
        repository.loadRestaurants()
        advanceUntilIdle()

        repository.toggleFavoriteRestaurant(id = 0, value = true)
        val restaurants = repository.getRestaurants()
        assert(restaurants.first {it.id == 0}.isFavorite)
    }

}