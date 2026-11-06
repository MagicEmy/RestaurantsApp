package com.example.restaurantsapp

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import com.example.restaurantsapp.restaurants.data.local.LocalRestaurant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RestaurantsRepositoryTest {

    @Test
    fun toggleFavorite_updatesFavoriteState() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val fakeRoomDao = FakeRoomDao()


        val originalRestaurant = LocalRestaurant(
            id = 1,
            title = "Test restaurant",
            description = "Test description",
            isFavorite = false
        )
        fakeRoomDao.addAll(
            listOf(originalRestaurant)
        )

        val repository = RestaurantsRepository(
            FakeApiService(),
            fakeRoomDao,
            dispatcher
        )
        repository.toggleFavoriteRestaurant(
            id = originalRestaurant.id,
            value = true
        )

        val updatedRestaurant =
            fakeRoomDao.getAll().single()

        assert(updatedRestaurant.isFavorite)
    }

}