package com.example.assignment6_parliament_room.ui.screens.detail

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
 * ViewModel for the detail screen.
 * Loads one MP and their ratings based on personNumber.
 */
class MpDetailViewModel(
    private val personNumber: Int,
    private val mpRepository: MpRepository,
    private val ratingRepository: RatingRepository
) : ViewModel() {

    // The MP being displayed (null while loading)
    val mp: StateFlow<MpEntity?> = mpRepository
        .getMpById(personNumber)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    // All ratings for this MP
    val ratings: StateFlow<List<RatingEntity>> = ratingRepository
        .getRatingsForMp(personNumber)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Controls whether the "add rating" dialog is shown
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun openDialog()  { _showDialog.value = true  }
    fun closeDialog() { _showDialog.value = false }

    fun addRating(isPositive: Boolean, comment: String) {
        viewModelScope.launch {
            ratingRepository.addRating(personNumber, isPositive, comment)
            _showDialog.value = false
        }
    }

    fun deleteRating(rating: RatingEntity) {
        viewModelScope.launch {
            ratingRepository.deleteRating(rating)
        }
    }

    // ── Factory ───────────────────────────────────────────────────────────────────

    class Factory(
        private val personNumber: Int,
        private val mpRepository: MpRepository,
        private val ratingRepository: RatingRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MpDetailViewModel(personNumber, mpRepository, ratingRepository) as T
        }
    }
}