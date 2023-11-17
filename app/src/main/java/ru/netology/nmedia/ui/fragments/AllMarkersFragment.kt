package ru.netology.nmedia.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.AllMarkersFragmentBinding
import ru.netology.nmedia.databinding.MarkerCardBinding
import ru.netology.nmedia.ui.dto.MyMarker
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel

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

        val adapter = MarkerAdapter()
        binding.list.adapter = adapter
        lifecycle.coroutineScope.launchWhenCreated {
            val markersList = viewModel.getMarkers()
            adapter.submitList(markersList)
        }
        binding.returnBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    class MarkerAdapter :
        ListAdapter<MyMarker, MarkerViewHolder>(PostDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
            val binding =
                MarkerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MarkerViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
            val post = getItem(position)
            holder.bind(post)
        }
    }

    class MarkerViewHolder(
        private val binding: MarkerCardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(marker: MyMarker) {
            binding.apply {
                markerTitle.text = marker.title
                location.text = marker.position
                location.setOnClickListener {
                    val intent = Intent()
                    intent.putExtra("location", location.toString())

                }
                content.text = marker.tag
                root.setOnClickListener {
                    //Навигация во фрагмент карты и переход по координатам
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
