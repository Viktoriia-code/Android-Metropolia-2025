package com.example.assignment6_parliament_room.data

import com.example.assignment6_parliament_room.data.local.RatingDao
import com.example.assignment6_parliament_room.data.local.RatingEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for user ratings.
 * Keeps rating logic separate from MP logic.
 */
class RatingRepository(
    private val ratingDao: RatingDao
) {

    // Get all ratings for one MP as a Flow (auto-updates UI)
    fun getRatingsForMp(personNumber: Int): Flow<List<RatingEntity>> =
        ratingDao.getRatingsForMp(personNumber)

    // Add a new rating
    suspend fun addRating(mpPersonNumber: Int, isPositive: Boolean, comment: String) {
        ratingDao.insert(
            RatingEntity(
                mpPersonNumber = mpPersonNumber,
                isPositive     = isPositive,
                comment        = comment
            )
        )
    }

    // Remove a rating
    suspend fun deleteRating(rating: RatingEntity) {
        ratingDao.delete(rating)
    }
}