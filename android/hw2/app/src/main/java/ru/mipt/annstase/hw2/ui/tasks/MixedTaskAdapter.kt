package ru.mipt.annstase.hw2.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw2.R
import ru.mipt.annstase.hw2.domain.model.TaskModel

class MixedTaskAdapter(
    private val onCheckedChange: (TaskModel, Boolean) -> Unit,
    private val onTaskClick: (TaskModel) -> Unit,
    private val onAdClose: () -> Unit
) : ListAdapter<MixedTaskAdapter.ListItem, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private const val TYPE_TASK  = 0
        private const val TYPE_AD    = 1
        private const val TYPE_EMPTY = 2

        private val DiffCallback = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(old: ListItem, new: ListItem) = when {
                old is ListItem.TaskItem  && new is ListItem.TaskItem  -> old.task.id == new.task.id
                old is ListItem.AdItem    && new is ListItem.AdItem    -> true
                old is ListItem.EmptyItem && new is ListItem.EmptyItem -> old.message == new.message
                else                                                    -> false
            }
            override fun areContentsTheSame(old: ListItem, new: ListItem) = old == new
        }
    }

    sealed class ListItem {
        data class TaskItem(val task: TaskModel) : ListItem()
        data object AdItem                          : ListItem()
        data class EmptyItem(val message: String) : ListItem()
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is ListItem.TaskItem  -> TYPE_TASK
        is ListItem.AdItem    -> TYPE_AD
        is ListItem.EmptyItem -> TYPE_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_TASK  -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_task, parent, false)
                TaskVH(v)
            }
            TYPE_AD    -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ad_banner, parent, false)
                AdVH(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_empty, parent, false)
                EmptyVH(v)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val it = getItem(position)) {
            is ListItem.TaskItem  -> (holder as TaskVH).bind(it.task)
            is ListItem.AdItem    -> (holder as AdVH).bind()
            is ListItem.EmptyItem -> (holder as EmptyVH).bind(it.message)
        }
    }

    inner class TaskVH(v: View) : RecyclerView.ViewHolder(v) {
        private val tvTitle: TextView = v.findViewById(R.id.tvTitle)
        private val cbDone: CheckBox = v.findViewById(R.id.cbDone)
        fun bind(task: TaskModel) {
            tvTitle.text = task.title
            cbDone.setOnCheckedChangeListener(null)
            cbDone.isChecked = task.done
            cbDone.setOnCheckedChangeListener { _, done -> onCheckedChange(task, done) }
            itemView.setOnClickListener { onTaskClick(task) }
        }
    }

    inner class AdVH(v: View) : RecyclerView.ViewHolder(v) {
        private val btnClose: AppCompatImageButton = v.findViewById(R.id.btnCloseAd)
        fun bind() {
            btnClose.setOnClickListener { onAdClose() }
        }
    }

    class EmptyVH(v: View) : RecyclerView.ViewHolder(v) {
        private val tvEmpty: TextView = v.findViewById(R.id.tvEmpty)
        fun bind(msg: String) {
            tvEmpty.text = msg
        }
    }
}
