package ru.mipt.annstase.hw3.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ru.mipt.annstase.hw3.R
import ru.mipt.annstase.hw3.databinding.DialogCreateChatBinding
import ru.mipt.annstase.hw3.viewmodel.CreateChatViewModel

class CreateChatDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "CreateChatDialog"
        const val REQUEST_KEY = "REQ_CREATE_CHAT"
        const val KEY_RESULT_NAME = "RESULT_CHAT_NAME"
    }

    private val viewModel: CreateChatViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogCreateChatBinding.inflate(layoutInflater)
        binding.editChatName.setText(viewModel.draftName.value)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(getString(R.string.create), null)
            .setNegativeButton(getString(R.string.cancel)) { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.isEnabled = binding.editChatName.text.toString().isNotBlank()
            binding.editChatName.doOnTextChanged { text, _, _, _ ->
                positiveButton.isEnabled = !text.isNullOrBlank()
                viewModel.onDraftChanged(text.toString())
            }
            positiveButton.setOnClickListener {
                val name = binding.editChatName.text.toString()
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(KEY_RESULT_NAME to name)
                )
                dialog.dismiss()
            }
        }
        return dialog
    }
}
