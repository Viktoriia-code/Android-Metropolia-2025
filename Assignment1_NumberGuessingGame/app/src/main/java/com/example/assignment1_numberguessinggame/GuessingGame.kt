package com.example.assignment1_numberguessinggame

import kotlin.random.Random

class GuessingGame {
    private var secretNumber = Random.nextInt(1, 101)
    var guesses = 0
        private set

    fun guess(number: Int): String {

        guesses++

        return when {
            number == secretNumber ->
                "$number is correct! You guessed in $guesses tries."

            number < secretNumber ->
                "$number is too low, try again."

            else ->
                "$number is too high, try again."
        }
    }

    fun isCorrect(number: Int): Boolean {
        return number == secretNumber
    }

    fun newGame() {
        secretNumber = Random.nextInt(1, 101)
        guesses = 0
    }
}