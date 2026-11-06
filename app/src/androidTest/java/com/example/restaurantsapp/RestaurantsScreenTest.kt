package com.example.restaurantsapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.presentation.Description
import com.example.restaurantsapp.restaurants.presentation.DummyContent
import com.example.restaurantsapp.restaurants.presentation.list.RestaurantsScreen
import com.example.restaurantsapp.restaurants.presentation.list.RestaurantsScreenState
import com.example.restaurantsapp.ui.theme.RestaurantsAppTheme
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RestaurantsScreenTest {
    @get:Rule
    val testRule: ComposeContentTestRule =
        createComposeRule()

    @Composable
    private fun TestRestaurantsScreen(
        state: RestaurantsScreenState,
        restaurants: List<Restaurant> = emptyList(),
        onFavoriteClick: (id: Int, oldValue: Boolean) -> Unit = { _, _ -> },
        onItemClick: (id: Int) -> Unit = {},
        onRetryRemote: () -> Unit = {}
    ) {
        val restaurantsFlow = remember(restaurants) {
            flowOf(PagingData.from(restaurants))
        }

        val pagingItems =
            restaurantsFlow.collectAsLazyPagingItems()

        RestaurantsScreen(
            state = state,
            restaurants = pagingItems,
            onFavoriteClick = onFavoriteClick,
            onItemClick = onItemClick,
            onRetryRemote = onRetryRemote
        )
    }

    @Test
    fun initialState_isRendered() {
        testRule.setContent {
            RestaurantsAppTheme {
                TestRestaurantsScreen(
                    state = RestaurantsScreenState(
                        isLoading = true),
                )
            }
        }
        testRule.onNodeWithContentDescription(
            Description.RESTAURANTS_LOADING
        ).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered() {
        val restaurants = DummyContent.getDomainRestaurants()
        testRule.setContent {
            RestaurantsAppTheme {
                TestRestaurantsScreen(
                    state = RestaurantsScreenState(
                        isLoading = false),
                    restaurants = restaurants
                )
            }
        }
        testRule.onNodeWithText(restaurants[0].title)
            .assertIsDisplayed()
        testRule.onNodeWithText(restaurants[0].description)
            .assertIsDisplayed()
        testRule.onNodeWithContentDescription(
            Description.RESTAURANTS_LOADING
        ).assertDoesNotExist()
    }

    @Test
    fun errorState_isRendered() {
        testRule.setContent {
            RestaurantsAppTheme {
                TestRestaurantsScreen(
                    state = RestaurantsScreenState(
                        isLoading = false,
                        error = "error text test"),
                )
            }
        }
        testRule.onNodeWithText("error text test")
            .assertIsDisplayed()
        testRule.onNodeWithContentDescription(
            Description.RESTAURANTS_LOADING)
            .assertDoesNotExist()
    }

    @Test
    fun stateWithContent_ClickOnItem_isRegistered() {
        val restaurants = DummyContent.getDomainRestaurants()
        val targetRestaurant = restaurants[0]
        var clickedRestaurantId: Int? = null
        testRule.setContent {
            RestaurantsAppTheme{
                TestRestaurantsScreen(
                    state = RestaurantsScreenState(
                        isLoading = false),
                    restaurants = restaurants,
                    onItemClick = { id ->
                        clickedRestaurantId = id                    }
                )
            }
        }
        testRule.onNodeWithText(targetRestaurant.title)
            .performClick()
        assertEquals(
            targetRestaurant.id,
            clickedRestaurantId
        )
    }

    @Test
    fun remoteError_retryClick_isRegistered() {
        var retryWasRequested = false

        testRule.setContent {
            RestaurantsAppTheme {
                TestRestaurantsScreen(
                    state = RestaurantsScreenState(
                        isLoading = false,
                        error = "Something went wrong."
                    ),
                    onRetryRemote = {
                        retryWasRequested = true
                    }
                )
            }
        }

        testRule.onNodeWithText("Retry")
            .performClick()

        assertTrue(retryWasRequested)
    }

}