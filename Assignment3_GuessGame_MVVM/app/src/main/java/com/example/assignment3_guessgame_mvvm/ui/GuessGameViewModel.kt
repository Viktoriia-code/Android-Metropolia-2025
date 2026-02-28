package com.example.assignment3_guessgame_mvvm.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GuessGameViewModel : ViewModel() {
    private var game = GuessGame()

    var message by mutableStateOf(
        "Guess a number between ${game.low} and ${game.high}"
    )
        private set

    var gameOver by mutableStateOf(false)
        private set

    var attempts by mutableStateOf(0)
        private set

    fun submitGuess(text: String) {

        val guess = text.toIntOrNull()

        if (guess == null) {
            message = "Enter a valid number"
            return
        }

        if (guess < game.low || guess > game.high) {
            message = "Enter a number between ${game.low} and ${game.high}"
            return
        }

        val result = game.makeGuess(guess)
        attempts = game.guessesMade

        message = when (result) {

            GuessResult.Low ->
                "$guess is too small"

            GuessResult.High ->
                "$guess is too big"

            GuessResult.Hit -> {
                gameOver = true
                "$guess is correct! Attempts: $attempts"
            }
        }
    }

    fun newGame() {
        game = GuessGame()
        attempts = 0
        gameOver = false

        message =
            "Guess a number between ${game.low} and ${game.high}"
    }
}