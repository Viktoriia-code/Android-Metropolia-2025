package com.example.assignment6_parliament_room.ui.screens.ratings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment6_parliament_room.data.local.mp.MpEntity
import com.example.assignment6_parliament_room.data.local.rating.RatingEntity
import com.example.assignment6_parliament_room.data.repository.MpRepository
import com.example.assignment6_parliament_room.data.repository.RatingRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Represents one rating combined with the MP it belongs to.
 * Used to show MP info next to each rating in the list.
 */
data class RatingWithMp(
    val rating: RatingEntity,
    val mp: MpEntity?           // null only if the MP was somehow removed from the database
)

/**
 * ViewModel for the My Ratings screen.
 * Combines all ratings with their corresponding MP data into a single list.
 */
class MyRatingsViewModel(
    private val ratingRepository: RatingRepository,
    private val mpRepository: MpRepository
) : ViewModel() {

    /**
     * All ratings joined with their MP info, newest first.
     *
     * combine() merges two Flows: the full ratings list and the full MP list.
     * Whenever either changes (e.g. a rating is deleted), the UI updates automatically.
     */
    val ratingsWithMp: StateFlow<List<RatingWithMp>> =
        combine(
            ratingRepository.getAllRatings(),
            mpRepository.getAllMps()
        ) { ratings, mps ->
            // Build a map for quick lookup: personNumber → MpEntity
            val mpMap = mps.associateBy { it.personNumber }
            ratings.map { rating ->
                RatingWithMp(
                    rating = rating,
                    mp     = mpMap[rating.mpPersonNumber]
                )
            }
        }.stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** Deletes a single rating from the database. */
    fun deleteRating(rating: RatingEntity) {
        viewModelScope.launch {
            ratingRepository.deleteRating(rating)
        }
    }

    // ── Factory ───────────────────────────────────────────────────────────────

    class Factory(
        private val ratingRepository: RatingRepository,
        private val mpRepository: MpRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MyRatingsViewModel(ratingRepository, mpRepository) as T
        }
    }
}