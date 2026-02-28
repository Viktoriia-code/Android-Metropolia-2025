package com.example.assignment3_guessgame_mvvm.ui

class GuessGame(val low: Int = 1, val high: Int = 20) {
    private val secret: Int
    private val guesses: MutableList<Int>

    init {
        require(low <= high)
        secret = (low..high).random()
        guesses = mutableListOf()
    }

    fun makeGuess(guess: Int): GuessResult {
        guesses.add(guess)
        return when {
            guess < secret -> GuessResult.Low
            guess > secret -> GuessResult.High
            else -> GuessResult.Hit
        }
    }

    val guessesMade
        get() = guesses.size
}

enum class GuessResult {
    Low, Hit, High
}