package com.example.mathgame.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult (
    val isVictory: Boolean,
    val countRightAnswers: Int,
    val countOfQuestions: Int,
    val gameSettings: GameSettings
): Parcelable {

    fun percent(): Int {
        if (countOfQuestions == 0) return 0
        return ((countRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }
}