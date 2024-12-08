package com.example.mathgame.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mathgame.R
import com.example.mathgame.databinding.FragmentGameFinishedBinding
import com.example.mathgame.domain.entity.GameResult


class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val percent = ((gameResult.countRightAnswers /
                gameResult.countOfQuestions.toDouble()) * 100).toInt()

        with(binding) {
            if (gameResult.isVictory)
                imgResult.setImageResource(R.drawable.winner_icon)
            else
                imgResult.setImageResource(R.drawable.looser_icon)

            tvScoreAnswers.text = context?.resources?.getString(
                R.string.text_your_score,
                gameResult.countRightAnswers.toString()
            )
            tvScorePercentage.text = context?.resources?.getString(
                R.string.text_your_percent_score,
                percent.toString()
            )
            tvRequiredPercentage.text = context?.resources?.getString(
                R.string.text_min_percent_correct_answers,
                gameResult.gameSettings.minPercentOfRightAnswers.toString()
            )
            tvCountCorrectAnswers.text = context?.resources?.getString(
                R.string.text_count_correct_answers,
                gameResult.gameSettings.minSumOfRightAnswers.toString()
            )
        }


        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    fun parseArgs() {
        gameResult = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_RESULT, GameResult::class.java)
        } else {
            requireArguments().getParcelable(KEY_RESULT) as? GameResult
        } ?: throw RuntimeException("Level is null")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val KEY_RESULT = "result"

        fun newInstance(result: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESULT, result)
                }
            }
        }

    }
}