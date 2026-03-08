package com.example.assignment6_parliament_room.data.local.rating

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RatingDao {

    @Insert
    suspend fun insert(rating: RatingEntity)

    @Delete
    suspend fun delete(rating: RatingEntity)

    // Get all ratings for one MP, newest first
    @Query("SELECT * FROM ratings WHERE mpPersonNumber = :personNumber ORDER BY timestamp DESC")
    fun getRatingsForMp(personNumber: Int): Flow<List<RatingEntity>>

    // Get ALL ratings across all MPs, newest first
    @Query("SELECT * FROM ratings ORDER BY timestamp DESC")
    fun getAllRatings(): Flow<List<RatingEntity>>
}