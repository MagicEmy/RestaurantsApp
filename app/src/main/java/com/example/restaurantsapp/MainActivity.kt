package com.example.restaurantsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.restaurantsapp.ui.theme.RestaurantsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantsAppTheme {
                RestaurantsApp()
            }
        }
    }

    @Composable
    private fun RestaurantsApp() {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "restaurants") {
            composable(route = "restaurants") {
                RestaurantsScreen { id ->
                    navController.navigate("restaurants/$id")
                }
            }
            composable(
                route = "restaurants/{restaurant_id}",
                arguments = listOf(navArgument("restaurant_id") {
                    type = NavType.IntType
                }),
                deepLinks = listOf(navDeepLink {
                    uriPattern = "https://www.restaurantsapp.details.com/{restaurant_id}"
                }),
            ) {
                RestaurantDetailsScreen()
            }
        }
    }
}