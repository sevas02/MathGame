package com.example.mathgame.presentation.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.green
import androidx.lifecycle.ViewModelProvider
import com.example.mathgame.R
import com.example.mathgame.databinding.FragmentGameBinding
import com.example.mathgame.domain.entity.GameResult
import com.example.mathgame.domain.entity.GameSettings
import com.example.mathgame.domain.entity.Level
import com.example.mathgame.presentation.viewModels.GameViewModel

class GameFragment : Fragment() {

    private lateinit var level: Level
    private lateinit var viewModel: GameViewModel

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

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
        setObservers(viewModel)
        viewModel.startGame(level)

        with (binding) {
            tvOption1.setOnClickListener {
                val answer =  tvOption1.text.toString().toInt()
                viewModel.takeAnswer(answer)
            }
            tvOption2.setOnClickListener {
                val answer = tvOption2.text.toString().toInt()
                viewModel.takeAnswer(answer)
            }
            tvOption3.setOnClickListener {
                val answer = tvOption3.text.toString().toInt()
                viewModel.takeAnswer(answer)
            }
            tvOption4.setOnClickListener {
                val answer = tvOption4.text.toString().toInt()
                viewModel.takeAnswer(answer)
            }
            tvOption5.setOnClickListener {
                val answer= tvOption5.text.toString().toInt()
                viewModel.takeAnswer(answer)
            }
            tvOption6.setOnClickListener {
                val answer= tvOption6.text.toString().toInt()
                viewModel.takeAnswer(answer)
            }
        }
    }

    private fun setObservers(viewModel: GameViewModel) {
        viewModel.timerLD.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

        viewModel.questionValue.observe(viewLifecycleOwner) {
            with(binding) {
                tvOption1.text = it.options[0].toString()
                tvOption2.text = it.options[1].toString()
                tvOption3.text = it.options[2].toString()
                tvOption4.text = it.options[3].toString()
                tvOption5.text = it.options[4].toString()
                tvOption6.text = it.options[5].toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                tvSumma.text = it.sum.toString()
            }
        }

        viewModel.isGameOver.observe(viewLifecycleOwner) {
            launchGameResultFragment()
        }

        viewModel.countCorrectAnswers.observe(viewLifecycleOwner) {
            binding.tvAnswersProgress.text = it
        }

        viewModel.percentCorrectAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
            Log.d("percent", it.toString())
        }

        viewModel.secondaryProgression.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }

        viewModel.isGoodPercent.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.progressDrawable.setColorFilter(
                    Color.GREEN,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            else {
                binding.progressBar.progressDrawable.setColorFilter(
                    Color.RED,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }

        viewModel.isGoodCount.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvAnswersProgress.setTextColor(resources.getColor(R.color.green))
            }
            else {
                binding.tvAnswersProgress.setTextColor(resources.getColor(R.color.red))
            }
        }
    }

    private fun launchGameResultFragment() {
        val gs = GameSettings(10, 0, 0, 5)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container,
                GameFinishedFragment.newInstance(
                    GameResult(true,10,10,gs)
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