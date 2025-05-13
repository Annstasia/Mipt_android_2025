package ru.mipt.annstase.hw3.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_DRAFT = "KEY_DRAFT_NAME"
    }

    val draftName: MutableLiveData<String> =
        savedStateHandle.getLiveData(KEY_DRAFT, "")

    fun onDraftChanged(text: String) {
        draftName.value = text
        savedStateHandle[KEY_DRAFT] = text
    }
}