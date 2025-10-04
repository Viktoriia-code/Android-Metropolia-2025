package com.example.assignment2_lotto.ui

data class LottoUiState (
    val selectedNumbers: List<Int> = emptyList(),
    val winningNumbers: Set<Int> = emptySet()
)