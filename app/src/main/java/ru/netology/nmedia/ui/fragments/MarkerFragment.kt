package ru.netology.nmedia.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.MarkerFragmentBinding
import ru.netology.nmedia.ui.AndroidUtils
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel

class MarkerFragment : Fragment() {
    private var savedTagText: String = ""
    private val viewModel: MarkerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = MarkerFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        binding.returnBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
        val editText = binding.markerTagField

        binding.saveButton.setOnClickListener {

            with(binding.markerTagField) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                } else {
//                    viewModel.changeContent(text.toString())
//                    viewModel.save()
                    setText("")
                    clearFocus()
                    AndroidUtils.hideKeyboard(this)
                }
            }
        }
        return binding.root
    }
}