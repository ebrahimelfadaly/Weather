package com.example.weatherapplication.model.favorite

import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("SELECT * FROM Favorite")
    fun getAll(): List<FavoriteEntity>

    @Delete
    suspend fun delete(favorite: FavoriteEntity)
}