package ru.mipt.annstase.hw3.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mipt.annstase.hw3.databinding.ItemMessageBinding
import ru.mipt.annstase.hw3.domain.model.Message

class MessagesAdapter : ListAdapter<Message, MessagesAdapter.MessageVH>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageVH {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageVH(binding)
    }

    override fun onBindViewHolder(holder: MessageVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MessageVH(private val b: ItemMessageBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(message: Message) {
            b.tvMessageText.text = message.name
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(old: Message, new: Message): Boolean =
                old.id == new.id

            override fun areContentsTheSame(old: Message, new: Message): Boolean =
                old == new
        }
    }
}
