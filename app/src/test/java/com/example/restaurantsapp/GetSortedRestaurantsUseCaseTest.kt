package com.example.restaurantsapp

import com.example.restaurantsapp.restaurants.data.RestaurantsRepository
import com.example.restaurantsapp.restaurants.domain.GetSortedRestaurantsUseCase
import com.example.restaurantsapp.restaurants.presentation.DummyContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GetSortedRestaurantsUseCaseTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)
    @Test
    fun listRestaurants_isSorted() =
        scope.runTest {
        val restaurantsRepository = RestaurantsRepository(
            FakeApiService(),
            FakeRoomDao(),
            dispatcher
        )
            restaurantsRepository.loadRestaurants()
            advanceUntilIdle()
            val getSortedRestaurantsUseCase = GetSortedRestaurantsUseCase(restaurantsRepository)
            val sortedRestaurants = getSortedRestaurantsUseCase()

            val restaurants = DummyContent.getDomainRestaurants().sortedBy { it.title }
            assert(sortedRestaurants == restaurants)
            
    }
}