package com.example.restaurantsapp.restaurants.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.paging.PagingSource

@Dao
interface RestaurantsDao {
    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<LocalRestaurant>
    @Query(
        """
            SELECT * FROM restaurants
            ORDER BY r_title COLLATE NOCASE ASC, r_id ASC
        """
    )
    fun getRestaurantsPagingSource(): PagingSource<Int, LocalRestaurant>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<LocalRestaurant>)
    @Update(entity = LocalRestaurant::class)
    suspend fun update(partialRestaurant: PartialLocalRestaurant)
    @Update(entity = LocalRestaurant::class)
    suspend fun updateAll(partialRestaurants: List<PartialLocalRestaurant>)
    @Query("SELECT * FROM restaurants WHERE is_favorite = 1")
    suspend fun getAllFavorite(): List<LocalRestaurant>

}