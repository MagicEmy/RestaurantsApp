package com.example.restaurantsapp.restaurants.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.presentation.details.RestaurantDetailsViewModel
import com.example.restaurantsapp.restaurants.presentation.list.RestaurantDetails
import com.example.restaurantsapp.restaurants.presentation.list.RestaurantIcon
import com.example.restaurantsapp.ui.theme.RestaurantsAppTheme

@Composable
fun RestaurantDetailsScreen(state: RestaurantDetailsScreenState ) {

    val item = state.restaurant

    if (item != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            RestaurantIcon(
                Icons.Filled.Place,
                Modifier.padding(
                    top = 32.dp,
                    bottom = 32.dp
                )
            )
            RestaurantDetails(
                item.title,
                item.description,
                Modifier.padding(bottom = 32.dp),
                Alignment.CenterHorizontally
            )
            Text("More info coming soon")
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