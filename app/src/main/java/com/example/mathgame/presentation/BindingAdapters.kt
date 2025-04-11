package com.example.mathgame.presentation

import android.graphics.Color
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mathgame.R

interface OnOptionClickListener {

    fun onOptionClick(option: Int)
}

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

@BindingAdapter("enoughCount")
fun bindEnoughCount(tv: TextView, enough: Boolean) {
    if (enough) {
        tv.setTextColor(tv.context.resources.getColor(R.color.green))
    } else {
        tv.setTextColor(tv.context.resources.getColor(R.color.red))
    }
}

@BindingAdapter("enoughPercent")
fun bindEnoughPercent(progressBar: ProgressBar, enough: Boolean) {
    if (enough) {
        progressBar.progressDrawable.setColorFilter(
            Color.GREEN,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    } else {
        progressBar.progressDrawable.setColorFilter(
            Color.RED,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(tv: TextView, number: Int) {
    tv.text = String.format(number.toString())
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(tv: TextView, clickListener: OnOptionClickListener) {
    tv.setOnClickListener {
        clickListener.onOptionClick(tv.text.toString().toInt())
    }
}