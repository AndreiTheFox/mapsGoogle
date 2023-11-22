package ru.netology.nmedia.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.AllMarkersFragmentBinding
import ru.netology.nmedia.databinding.MarkerCardBinding
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel


typealias OnMarkListener = (marker: MyMarker) -> Unit

class AllMarkersFragment : Fragment() {
    private val viewModel: MarkerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = AllMarkersFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = MarkerAdapter {
            //Отправка данных о локации кликнутого маркера
            setFragmentResult(
                "location",
                bundleOf("location" to it.position)
            )
            findNavController().navigate(R.id.action_allMarkersFragment_to_mapsFragment)
        }
        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMarkers().collect(adapter::submitList)
            }
        }
        binding.returnBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }


    class MarkerAdapter(private val onMarkListener: OnMarkListener) :
        ListAdapter<MyMarker, MarkerViewHolder>(PostDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
            val binding =
                MarkerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MarkerViewHolder(binding, onMarkListener)
        }

        override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
            val post = getItem(position)
            holder.bind(post)
        }
    }

    class MarkerViewHolder(
        private val binding: MarkerCardBinding,
        private val onMarkListener: OnMarkListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(marker: MyMarker) {
            binding.apply {
                markerTitle.text = marker.title
                location.text = marker.position
                location.setOnClickListener {
                    onMarkListener(marker)
                }
                content.text = marker.tag
                root.setOnClickListener {
                    onMarkListener(marker)
                }
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<MyMarker>() {
        override fun areItemsTheSame(oldItem: MyMarker, newItem: MyMarker): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MyMarker, newItem: MyMarker): Boolean {
            return oldItem == newItem
        }

    }
}
