package com.example.mathgame.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mathgame.R

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(tv: TextView, count: Int) {
    tv.text = tv.context?.resources?.getString(
        R.string.text_count_correct_answers,
        count.toString()
    )
}

@BindingAdapter("yourScore")
fun bindScoreAnswer(tv: TextView, count: Int) {
    tv.text = tv.context?.resources?.getString(
        R.string.text_your_score,
        count.toString()
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(tv: TextView, count: Int) {
    tv.text = tv.context?.resources?.getString(
        R.string.text_min_percent_correct_answers,
        count.toString()
    )
}

@BindingAdapter("yourPercent")
fun bindPercentAnswer(tv: TextView, percent: Int) {
    tv.text = tv.context?.resources?.getString(
        R.string.text_your_percent_score,
        percent.toString()
    )
}

@BindingAdapter("resultImage")
fun bindImageResult(image: ImageView, isVictory: Boolean) {
    if (isVictory)
        image.setImageResource(R.drawable.winner_icon)
    else
        image.setImageResource(R.drawable.looser_icon)
}
