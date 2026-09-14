package com.example.restaurantsapp

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer

data class Restaurant(
    @SerializedName("r_id")
    val id: Int,
    @SerializedName("r_title")
    val title: String,
    @SerializedName("r_description")
    val description: String,
    var isFavorite: Boolean = false
)

//Restaurants.json uploaded in firebase - change rules to 'true' to always read without update every 30 days


