package com.example.restaurantsapp.restaurants.presentation.details

import android.R.attr.icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.presentation.list.RestaurantDetails
import com.example.restaurantsapp.restaurants.presentation.list.RestaurantIcon
import com.example.restaurantsapp.ui.theme.RestaurantsAppTheme

@Composable
fun RestaurantDetailsScreen(
    state: RestaurantDetailsScreenState
) {
    val error = state.error
    val restaurant = state.restaurant

    when {
        state.isLoading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ){
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = error)
            }
        }

        restaurant != null -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                RestaurantIcon(
                    icon = Icons.Filled.Place,
                    modifier = Modifier.padding(
                        top = 32.dp,
                        bottom = 32.dp
                    )
                )

                RestaurantDetails(
                    title = restaurant.title,
                    description = restaurant.description,
                    modifier = Modifier.padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )

                Text(text = "More info coming soon")
            }
        }
    }
}

@Preview
@Composable
fun RestaurantDetailsScreenPreview() {
    RestaurantsAppTheme {
        RestaurantDetailsScreen(
            state = RestaurantDetailsScreenState(
                restaurant = Restaurant(
                    id = 1,
                    title = "Sample restaurant",
                    description = "This is a sample restaurant"
                )
            ))
    }
}