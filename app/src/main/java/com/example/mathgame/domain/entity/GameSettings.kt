package com.example.mathgame.domain.entity

data class GameSettings (
    val maxSumValue: Int,
    val minSumOfRightAnswers: Int,
    val minPercentOfRightAnswers: Int,
    val gameTimeInSeconds: Int
)