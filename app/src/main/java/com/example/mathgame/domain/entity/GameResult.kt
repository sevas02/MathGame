package com.example.mathgame.domain.entity

data class GameResult (
    val isVictory: Boolean,
    val countRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
)