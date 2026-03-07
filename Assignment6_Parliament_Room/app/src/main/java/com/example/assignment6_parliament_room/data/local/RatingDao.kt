package com.example.assignment6_parliament_room.data.local

import androidx.room.*
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
}