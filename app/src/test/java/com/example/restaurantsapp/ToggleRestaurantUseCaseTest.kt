package com.example.restaurantsapp

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import com.example.restaurantsapp.restaurants.data.local.LocalRestaurant
import com.example.restaurantsapp.restaurants.domain.ToggleRestaurantUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleRestaurantUseCaseTest {

    @Test
    fun toggleRestaurant_isUpdatingFavoriteField() =
        runTest {
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
            val restaurantsRepository = RestaurantsRepository(
                FakeApiService(),
                fakeRoomDao,
                dispatcher
            )
            val toggleRestaurantUseCase = ToggleRestaurantUseCase(restaurantsRepository)

            toggleRestaurantUseCase(
                id = originalRestaurant.id,
                oldValue = originalRestaurant.isFavorite
            )

            val updatedRestaurant =
                fakeRoomDao.getAll().single()

            assertEquals(
                true,
                updatedRestaurant.isFavorite
            )
        }
}