package com.example.mathgame.presentation.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.green
import androidx.lifecycle.ViewModelProvider
import com.example.mathgame.R
import com.example.mathgame.databinding.FragmentGameBinding
import com.example.mathgame.domain.entity.GameResult
import com.example.mathgame.domain.entity.GameSettings
import com.example.mathgame.domain.entity.Level
import com.example.mathgame.presentation.viewModels.GameViewModel
import com.example.mathgame.presentation.viewModels.GameViewModelFactory

class GameFragment : Fragment() {

    private lateinit var level: Level
    private lateinit var viewModel: GameViewModel

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            GameViewModelFactory(
                requireActivity().application,
                level
            )
        )[GameViewModel::class.java]
        setObservers(viewModel)
        setOnClickOptionsListeners()
    }

    private fun setOnClickOptionsListeners() {
        for (option in tvOptions) {
            option.setOnClickListener {
                val answer = option.text.toString().toInt()
                viewModel.takeAnswer(answer)
            }
        }
    }

    private fun setObservers(viewModel: GameViewModel) {
        viewModel.timerLD.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        viewModel.questionValue.observe(viewLifecycleOwner) {
            for ((idx, option) in tvOptions.withIndex()) {
                option.text = it.options[idx].toString()
            }
            with(binding) {
                tvLeftNumber.text = it.visibleNumber.toString()
                tvSumma.text = it.sum.toString()
            }
        }

        viewModel.gameResultLD.observe(viewLifecycleOwner) {
            launchGameResultFragment(it)
        }

        viewModel.countCorrectAnswers.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.text = it
        }

        viewModel.percentCorrectAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }

        viewModel.secondaryProgression.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }

        //Установка цветового сопровождения для прогресс бара
        viewModel.isGoodPercent.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.progressDrawable.setColorFilter(
                    Color.GREEN,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.progressBar.progressDrawable.setColorFilter(
                    Color.RED,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }

        //установка цветового сопровождения для текста кол-ва правильных ответов
        viewModel.isGoodCount.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvAnswersProgress.setTextColor(resources.getColor(R.color.green))
            } else {
                binding.tvAnswersProgress.setTextColor(resources.getColor(R.color.red))
            }
        }
    }

    private fun launchGameResultFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_container,
                GameFinishedFragment.newInstance(
                    gameResult
                )
            )
            .addToBackStack(null)
            .commit()
    }

    private fun parseArgs() {
        level = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_LEVEL, Level::class.java)
        } else {
            requireArguments().getParcelable(KEY_LEVEL) as? Level
        } ?: throw RuntimeException("Level is null")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        private const val KEY_LEVEL = "level"
        const val NAME = "fragment_name_game"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}