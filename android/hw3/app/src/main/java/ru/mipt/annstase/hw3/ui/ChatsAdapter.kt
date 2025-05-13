package ru.mipt.annstase.hw3.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.mipt.annstase.hw3.R
import ru.mipt.annstase.hw3.databinding.ItemChatBinding
import ru.mipt.annstase.hw3.domain.model.Chat

class ChatsAdapter(
    private val onClick: (Chat) -> Unit
) : ListAdapter<Chat, ChatsAdapter.ChatVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        val binding = ItemChatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatVH(binding)
    }

    override fun onBindViewHolder(holder: ChatVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatVH(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            binding.tvName.text = chat.name
            Glide.with(binding.chatImage)
                .load(binding.root.context.getString(R.string.chat_avatar_image))
                .circleCrop()
                .into(binding.chatImage)

            binding.root.setOnClickListener {
                onClick(chat) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(a: Chat, b: Chat) = a.id == b.id
            override fun areContentsTheSame(a: Chat, b: Chat) = a == b
        }
    }
}
