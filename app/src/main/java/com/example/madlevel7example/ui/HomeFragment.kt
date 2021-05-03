package com.example.madlevel7example.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madlevel7example.vm.QuizViewModel
import com.example.madlevel7example.R
import com.example.madlevel7example.databinding.FragmentHomeBinding

// A simple [Fragment] subclass as the default destination in the navigation.
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        // Always retrieve quiz when screen is shown.
        viewModel.getQuiz()

        binding.btCreateQuiz.setOnClickListener {
            navController.navigate(R.id.action_HomeFragment_to_CreateQuizFragment)
        }

        viewModel.quiz.observe(viewLifecycleOwner, {
            // Make button visible and clickable.
            if (!it.question.isBlank() || !it.answer.isBlank()) {
                binding.btStartQuiz.alpha = 1.0f
                binding.btStartQuiz.isClickable = true

                binding.btStartQuiz.setOnClickListener {
                    navController.navigate(R.id.action_HomeFragment_to_quizFragment)
                }
            }
        })
    }
}