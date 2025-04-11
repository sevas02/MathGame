package com.example.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private var _binding: FragmentGameBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setObservers(viewModel)
    }

    private fun setObservers(viewModel: GameViewModel) {
        viewModel.gameResultLD.observe(viewLifecycleOwner) {
            launchGameResultFragment(it)
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