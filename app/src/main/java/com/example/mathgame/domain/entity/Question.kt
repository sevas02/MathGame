package com.example.mathgame.domain.entity


data class Question (
    val sum: Int,
    val visibleNumber: Int,
    val options: List<Int>
) {
    val correctAnswer
        get() = sum - visibleNumber
}