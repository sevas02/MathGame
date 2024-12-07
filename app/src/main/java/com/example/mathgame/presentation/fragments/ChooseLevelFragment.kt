package com.example.mathgame.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mathgame.R
import com.example.mathgame.databinding.FragmentChooseLevelBinding
import com.example.mathgame.domain.entity.Level

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnTestLvl.setOnClickListener {
                launchGame(Level.TEST)
            }

            btnEasyLvl.setOnClickListener {
                launchGame(Level.EASY)
            }

            btnNormalLvl.setOnClickListener {
                launchGame(Level.NORMAL)
            }

            btnHardLvl.setOnClickListener {
                launchGame(Level.HARD)
            }
        }
    }

    fun launchGame(level: Level) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFragment.newInstance(level))
            .addToBackStack(GameFragment.NAME)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        const val NAME = "fragment_name_choose_level"

        fun newInstnce(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }

}