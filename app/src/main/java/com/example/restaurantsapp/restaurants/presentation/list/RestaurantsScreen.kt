package com.example.restaurantsapp.restaurants.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.presentation.Description
import com.example.restaurantsapp.ui.theme.RestaurantsAppTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun RestaurantsScreen(
    state: RestaurantsScreenState,
    restaurants: LazyPagingItems<Restaurant>,
    onItemClick: (id: Int) -> Unit,
    onFavoriteClick: (id: Int, oldValue: Boolean) -> Unit,
    onRetryRemote: () -> Unit
) {
    val pagingRefreshState =
        restaurants.loadState.refresh

    val isInitialLoading =
        restaurants.itemCount == 0 && (
                state.isLoading || pagingRefreshState is LoadState.Loading
                )
    val initialError =
        state.error ?: (
                pagingRefreshState as? LoadState.Error)
            ?.error
            ?.message
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 8.dp
            )
        ) {
            items(
                count = restaurants.itemCount,
                key = restaurants.itemKey { restaurant ->
                    restaurant.id
                }
            ) { index ->
                val restaurant = restaurants[index]
                if (restaurant != null) {
                    RestaurantsItem(
                        item = restaurant,
                        onFavoriteClick = onFavoriteClick,
                        onItemClick = onItemClick
                    )
                }
            }

            when (
                val appendState = restaurants.loadState.append
            ) {
                is LoadState.Loading -> {
                    item(key = "append_loading") {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is LoadState.Error -> {
                    item(key = "append_error") {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = appendState.error.message
                                    ?: "Unable to load more restaurants.",
                                color = MaterialTheme.colorScheme.error,
                            )
                            Button(
                                onClick = {
                                    restaurants.retry()
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                is LoadState.NotLoading -> Unit
            }
        }

        if (isInitialLoading) {
            CircularProgressIndicator(
                Modifier.semantics {
                    contentDescription =
                        Description.RESTAURANTS_LOADING
                }
            )
        }
        if (
            initialError != null &&
            restaurants.itemCount == 0
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ){
                Text(
                    text = initialError,
                    color = MaterialTheme.colorScheme.error
                )
                Button(
                    onClick = {
                        if (state.error != null) {
                            onRetryRemote()
                        } else {
                            restaurants.retry()
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ){
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
fun RestaurantsItem(item: Restaurant,
                    onFavoriteClick: (id: Int, oldValue: Boolean) -> Unit,
                    onItemClick: (id: Int) -> Unit) {
    val icon = if (item.isFavorite)
        Icons.Filled.Favorite
    else
        Icons.Filled.FavoriteBorder
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClick(item.id)}
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp),
        ) {
            RestaurantIcon(
                Icons.Filled.Place,
                Modifier.weight(0.15f),
            )
            RestaurantDetails(
                item.title,
                item.description,
                Modifier.weight(0.7f))
            RestaurantIcon(
                icon,
                Modifier.weight(0.15f)) {
                onFavoriteClick(item.id, item.isFavorite)
            }
        }
    }
}

@Composable
fun RestaurantIcon(icon: ImageVector,
                   modifier: Modifier,
                   onClick: () -> Unit = {}) {
    Image(
        imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() }
    )
}

@Composable
fun RestaurantDetails(title: String,
                      description: String,
                      modifier: Modifier,
                      horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun DefaultPreview() {
    val restaurants = flowOf(
        PagingData.empty<Restaurant>()
    ).collectAsLazyPagingItems()

    RestaurantsAppTheme {
        RestaurantsScreen(
            state = RestaurantsScreenState(
                isLoading = true
            ),
            restaurants = restaurants,
            onItemClick = {},
            onFavoriteClick = { _, _ -> },
            onRetryRemote = {}
        )
    }
}


