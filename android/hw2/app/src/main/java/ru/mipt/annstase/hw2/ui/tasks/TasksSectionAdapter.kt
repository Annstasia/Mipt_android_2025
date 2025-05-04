package ru.mipt.annstase.hw2.ui.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.domain.model.TaskModel

class TasksSectionAdapter(
    private val context: Context,
    private val onCheckedChange: (TaskModel, Boolean) -> Unit,
    private val onItemClick: (TaskModel) -> Unit
) : ListAdapter<TasksSectionAdapter.SectionItem, RecyclerView.ViewHolder>(DiffCallback) {

    sealed class SectionItem {
        data class Header(val title: String)   : SectionItem()
        data class TaskItem(val task: TaskModel): SectionItem()
        data class EmptyItem(val text: String) : SectionItem()
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TASK   = 1
        private const val TYPE_EMPTY  = 2

        private val DiffCallback = object : DiffUtil.ItemCallback<SectionItem>() {
            override fun areItemsTheSame(old: SectionItem, new: SectionItem) = when {
                old is SectionItem.Header    && new is SectionItem.Header    -> old.title == new.title
                old is SectionItem.TaskItem  && new is SectionItem.TaskItem  -> old.task.id == new.task.id
                old is SectionItem.EmptyItem && new is SectionItem.EmptyItem -> old.text == new.text
                else                                                       -> false
            }
            override fun areContentsTheSame(old: SectionItem, new: SectionItem) = old == new
        }
    }

    fun submitSections(today: List<TaskModel>, tomorrow: List<TaskModel>) {
        val items = mutableListOf<SectionItem>()

        items += SectionItem.Header(context.getString(R.string.tasks_today))
        if (today.isEmpty()) {
            items += SectionItem.EmptyItem(context.getString(R.string.freedom))
        } else {
            today.forEach { items += SectionItem.TaskItem(it) }
        }

        items += SectionItem.Header(context.getString(R.string.tasks_tomorrow))
        if (tomorrow.isEmpty()) {
            items += SectionItem.EmptyItem(context.getString(R.string.freedom))
        } else {
            tomorrow.forEach { items += SectionItem.TaskItem(it) }
        }

        submitList(items)
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is SectionItem.Header    -> TYPE_HEADER
        is SectionItem.TaskItem  -> TYPE_TASK
        is SectionItem.EmptyItem -> TYPE_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_HEADER -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_section_header, parent, false)
                HeaderViewHolder(v)
            }
            TYPE_TASK -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_task, parent, false)
                TaskViewHolder(v)
            }
            else -> {
                val tv = TextView(parent.context).apply {
                    setPadding(32, 24, 32, 24)
                    setTextAppearance(android.R.style.TextAppearance_Material_Body1)
                }
                object : RecyclerView.ViewHolder(tv) {}
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SectionItem.Header    -> (holder as HeaderViewHolder).bind(item.title)
            is SectionItem.TaskItem  -> (holder as TaskViewHolder).bind(item.task)
            is SectionItem.EmptyItem -> (holder.itemView as TextView).text = item.text
        }
    }

    private class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvHeader: TextView = view.findViewById(R.id.tvSectionHeader)
        fun bind(title: String) {
            tvHeader.text = title
        }
    }

    private inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        private val cbDone: CheckBox   = view.findViewById(R.id.cbDone)

        init {
            view.setOnClickListener {
                val task = (getItem(bindingAdapterPosition) as SectionItem.TaskItem).task
                onItemClick(task)
            }
        }

        fun bind(task: TaskModel) {
            tvTitle.text = task.title
            cbDone.setOnCheckedChangeListener(null)
            cbDone.isChecked = task.done
            cbDone.setOnCheckedChangeListener { _, checked ->
                onCheckedChange(task, checked)
            }
        }
    }
}
