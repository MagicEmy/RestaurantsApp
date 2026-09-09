package com.example.restaurantsapp

data class Restaurant(
    val id: Int,
    val title: String,
    val description: String,
    var isFavorite: Boolean = false
)

//Restaurants.json uploaded in firebase - change rules to 'true' to always read without update every 30 days
val dummyRestaurants = listOf(
    Restaurant(
        id = 1,
        title = "La Tavola Romana",
        description = "Traditional Roman dishes, homemade pasta, and seasonal ingredients."
    ),
    Restaurant(
        id = 2,
        title = "Green Garden",
        description = "Fresh vegetarian meals prepared with locally sourced ingredients."
    ),
    Restaurant(
        id = 3,
        title = "Sakura House",
        description = "Japanese cuisine featuring sushi, ramen, and small sharing dishes."
    ),
    Restaurant(
        id = 4,
        title = "Spice Route",
        description = "Indian curries, tandoori specialties, and fragrant rice dishes."
    ),
    Restaurant(
        id = 6,
        title = "Bella Napoli",
        description = "Traditional Neapolitan pizza made with fresh Italian ingredients."
    ),
    Restaurant(
        id = 7,
        title = "The Green Garden",
        description = "Fresh vegetarian and vegan dishes prepared with seasonal produce."
    ),
    Restaurant(
        id = 8,
        title = "Sakura Kitchen",
        description = "Authentic Japanese dishes, including sushi, ramen, and tempura."
    ),
    Restaurant(
        id = 9,
        title = "Spice of India",
        description = "Traditional Indian curries and aromatic dishes rich in spices."
    ),
    Restaurant(
        id = 10,
        title = "Burger House",
        description = "Homemade burgers served with crispy fries and signature sauces."
    ),
    Restaurant(
        id = 11,
        title = "Mediterranean Table",
        description = "Fresh Mediterranean dishes inspired by the cuisines of Southern Europe."
    ),
    Restaurant(
        id = 12,
        title = "Golden Dragon",
        description = "Traditional Chinese dishes prepared with fresh vegetables and aromatic spices."
    ),
    Restaurant(
        id = 13,
        title = "Taco Fiesta",
        description = "Colourful Mexican dishes, including tacos, burritos, and homemade salsa."
    )
)

