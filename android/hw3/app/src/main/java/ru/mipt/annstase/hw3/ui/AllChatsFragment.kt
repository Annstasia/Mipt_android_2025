package ru.mipt.annstase.hw3.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.mipt.annstase.hw3.R
import ru.mipt.annstase.hw3.databinding.AllChatsFragmentBinding
import ru.mipt.annstase.hw3.viewmodel.AllChatsViewModel


@AndroidEntryPoint
class AllChatsFragment : Fragment(R.layout.all_chats_fragment) {

    private var _binding: AllChatsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AllChatsViewModel by viewModels()
    private lateinit var adapter: ChatsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = AllChatsFragmentBinding.bind(view)

        adapter = ChatsAdapter { chat ->
            (requireActivity() as ChatSelectionListener).onChatSelected(chat.id)
        }
        binding.chats.layoutManager = LinearLayoutManager(requireContext())
        binding.chats.adapter = adapter

        parentFragmentManager.setFragmentResultListener(
            CreateChatDialogFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(CreateChatDialogFragment.KEY_RESULT_NAME)
                ?.takeIf(String::isNotBlank)
                ?.let { viewModel.createChat(it) }
        }

        binding.creatChatBtn.setOnClickListener {
            CreateChatDialogFragment()
                .show(requireActivity().supportFragmentManager, CreateChatDialogFragment.TAG)
        }

        viewModel.chats.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
