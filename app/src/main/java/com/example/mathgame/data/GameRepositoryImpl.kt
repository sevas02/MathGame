package com.example.mathgame.data

import com.example.mathgame.domain.entity.GameSettings
import com.example.mathgame.domain.entity.Level
import com.example.mathgame.domain.entity.Question
import com.example.mathgame.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl: GameRepository {

    const val MIN_QUESTION_VALUE = 2
    const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val options = HashSet<Int>()
        val questionValue = Random.nextInt(MIN_QUESTION_VALUE, maxSumValue + 1)
        val visibleValue = Random.nextInt(MIN_ANSWER_VALUE, questionValue)
        val correctAnswer = questionValue - visibleValue
        options.add(correctAnswer)
        val from = max(MIN_ANSWER_VALUE, correctAnswer - 9)
        val to = min(correctAnswer + 9, maxSumValue)
        while (options.size < countOfOptions){
            val newValue = Random.nextInt(from, to)
            options.add(newValue)
        }
        return Question(questionValue, visibleValue, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> {
                GameSettings(
                    10,
                    3,
                    50,
                    0
                )
            }
            Level.EASY -> {
                GameSettings(
                    10,
                    10,
                    70,
                    60
                )
            }
            Level.NORMAL -> {
                GameSettings(
                    20,
                    20,
                    80,
                    40
                )
            }
            Level.HARD -> {
                GameSettings(
                    30,
                    30,
                    90,
                    40
                )
            }
        }
    }
}