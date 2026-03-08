package com.example.assignment6_parliament_room.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.assignment6_parliament_room.data.local.mp.MpEntity
import com.example.assignment6_parliament_room.data.repository.MpRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class GroupBy { CONSTITUENCY, PARTY }

class MpListViewModel(private val repository: MpRepository) : ViewModel() {

    // Which grouping is currently selected (default: by constituency)
    private val _groupBy = MutableStateFlow(GroupBy.CONSTITUENCY)
    val groupBy: StateFlow<GroupBy> = _groupBy.asStateFlow()

    // True while loading data from network
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Non-null when an error occurred
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // To show only favorite MPs
    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    /**
     * The main data for the screen: a map of group name → list of MPs.
     * combine() merges two Flows into one - it re-runs whenever either changes.
     * stateIn() converts the Flow to a StateFlow that the UI can observe.
     */
    val groupedMps: StateFlow<Map<String, List<MpEntity>>> =
        combine(repository.getAllMps(), _groupBy, _showFavoritesOnly) { mps, grouping, favOnly ->
            val filtered = if (favOnly) mps.filter { it.isFavorite } else mps
            when (grouping) {
                GroupBy.CONSTITUENCY -> filtered.groupBy { it.constituency }.toSortedMap()
                GroupBy.PARTY        -> filtered.groupBy { it.party }.toSortedMap()
            }
        }.stateIn(
            scope        = viewModelScope,
            started      = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    init {
        // Load data when the ViewModel is first created
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.loadIfEmpty()
            } catch (e: Exception) {
                _error.value = "Failed to load data: ${e.localizedMessage}"
            }
            _isLoading.value = false
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.refresh()
            } catch (e: Exception) {
                _error.value = "Refresh failed: ${e.localizedMessage}"
            }
            _isLoading.value = false
        }
    }

    fun setGroupBy(value: GroupBy) {
        _groupBy.value = value
    }

    fun toggleFavoritesFilter() { _showFavoritesOnly.value = !_showFavoritesOnly.value }

    fun toggleFavorite(mp: MpEntity) {
        viewModelScope.launch {
            repository.setFavorite(mp.personNumber, !mp.isFavorite)
        }
    }

    // ── Factory ───────────────────────────────────────────────────────────────────

    /**
     * Factory is needed because our ViewModel has a constructor parameter (repository).
     * Without Hilt, we must provide the factory manually.
     */
    class Factory(private val repository: MpRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MpListViewModel(repository) as T
        }
    }
}