package com.example.restaurantsapp

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import com.example.restaurantsapp.restaurants.domain.GetSortedRestaurantsUseCase
import com.example.restaurantsapp.restaurants.domain.ToggleRestaurantUseCase
import com.example.restaurantsapp.restaurants.presentation.DummyContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class ToggleRestaurantUseCaseTest {
    private  val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)
    @Test
    fun toggleRestaurant_isUpdatingFavoriteField() =
        scope.runTest {
            val restaurantsRepository = RestaurantsRepository(
                FakeApiService(),
                FakeRoomDao(),
                dispatcher
            )
            val getSortedRestaurantsUseCase =
                GetSortedRestaurantsUseCase(restaurantsRepository)
            val useCase = ToggleRestaurantUseCase(
                restaurantsRepository,
                getSortedRestaurantsUseCase
            )
            restaurantsRepository.loadRestaurants()
            advanceUntilIdle()

            val restaurants = DummyContent.getDomainRestaurants()
            val targetItem = restaurants[0]
            val isFavorite = targetItem.isFavorite
            val updatedRestaurants = useCase(
                targetItem.id,
                isFavorite
            )
            advanceUntilIdle()

            restaurants[0] = targetItem.copy(isFavorite = !isFavorite)
            assert(updatedRestaurants == restaurants)
        }
}