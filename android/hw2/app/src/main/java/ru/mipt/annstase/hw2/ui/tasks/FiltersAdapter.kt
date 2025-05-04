package ru.mipt.annstase.hw2.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw2.databinding.ItemFilterBinding

class FiltersAdapter(
    private val onFilterClick: (FilterItem, Boolean) -> Unit
) : ListAdapter<FiltersAdapter.FilterItem, FiltersAdapter.FilterViewHolder>(DiffCallback) {

    data class FilterItem(val name: String, val isSelected: Boolean)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FilterItem>() {
            override fun areItemsTheSame(old: FilterItem, new: FilterItem) =
                old.name == new.name

            override fun areContentsTheSame(old: FilterItem, new: FilterItem) =
                old == new
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FilterViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterItem) {
            binding.btnFilter.text = item.name
            binding.btnFilter.isSelected = item.isSelected
            binding.btnFilter.setOnClickListener {
                onFilterClick(item, !item.isSelected)
            }
        }
    }
}
