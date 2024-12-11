package com.example.mathgame.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mathgame.R
import com.example.mathgame.databinding.FragmentGameBinding
import com.example.mathgame.domain.entity.GameResult
import com.example.mathgame.presentation.viewModels.GameViewModel
import com.example.mathgame.presentation.viewModels.GameViewModelFactory

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private val args by navArgs<GameFragmentArgs>()

    private val viewModelFactory by lazy {
        GameViewModelFactory(requireActivity().application, args.level)
    }

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
            viewModelFactory
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
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}