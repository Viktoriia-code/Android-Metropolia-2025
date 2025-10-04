package com.example.assignment2_lotto.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LottoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LottoUiState())
    val uiState: StateFlow<LottoUiState> = _uiState.asStateFlow()

    private val totalNumbers = 40
    private val pickCount = 7

    init {
        // Create winning numbers when ViewModel is initialized
        _uiState.value = _uiState.value.copy(
            winningNumbers = generateWinningNumbers()
        )
    }

    private fun generateWinningNumbers(): Set<Int> {
        val pool = (1..totalNumbers).toList()
        return pool.shuffled().take(pickCount).toSet()
    }

    fun toggleNumber(n: Int) {
        val current = _uiState.value
        val selected = current.selectedNumbers.toMutableList()

        if (selected.contains(n)) {
            selected.remove(n)
            _uiState.value = current.copy(
                selectedNumbers = selected,
                resultMessage = null
            )
        } else if (selected.size < pickCount) {
            selected.add(n)
            val newState = current.copy(selectedNumbers = selected)
            _uiState.value = if (selected.size == pickCount) {
                val correct = selected.count { it in current.winningNumbers }
                newState.copy(
                    resultMessage = "You matched $correct out of $pickCount numbers"
                )
            } else {
                newState
            }
        }
    }

    fun resetGame() {
        _uiState.value = LottoUiState(
            winningNumbers = generateWinningNumbers()
        )
    }
}
