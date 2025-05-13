package ru.mipt.annstase.hw3.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw3.R
import ru.mipt.annstase.hw3.databinding.FragmentChatDetailBinding
import ru.mipt.annstase.hw3.viewmodel.ChatDetailViewModel
import androidx.fragment.app.activityViewModels

@AndroidEntryPoint
class ChatDetailFragment : Fragment(R.layout.fragment_chat_detail) {

    companion object {
        private const val ARG_CHAT_ID = "chatId"
        fun newInstance(chatId: Int) = ChatDetailFragment().apply {
            arguments = Bundle().apply { putInt(ARG_CHAT_ID, chatId) }
        }
    }

    private val viewModel: ChatDetailViewModel by activityViewModels()

    private var _binding: FragmentChatDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MessagesAdapter

    private val chatId by lazy { requireArguments().getInt(ARG_CHAT_ID) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentChatDetailBinding.bind(view)

        adapter = MessagesAdapter()
        binding.messages.layoutManager = LinearLayoutManager(requireContext())
        binding.messages.adapter = adapter

        viewModel.loadMessages(chatId)
        viewModel.messages.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list) {
                if (list.isNotEmpty())
                    binding.messages.scrollToPosition(list.lastIndex)
            }
        }

        binding.editMessage.setText(viewModel.draftMessage.value)

        binding.editMessage.doOnTextChanged { text, _, _, _ ->
            viewModel.onDraftChanged(text.toString())
        }

        binding.btnSend.setOnClickListener {
            val text = binding.editMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(chatId, text)
                binding.editMessage.setText("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
