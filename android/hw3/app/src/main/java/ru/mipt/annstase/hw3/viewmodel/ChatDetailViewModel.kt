package ru.mipt.annstase.hw3.viewmodel

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mipt.annstase.hw3.domain.model.Message
import ru.mipt.annstase.hw3.domain.usecase.GetMessagesUseCase
import ru.mipt.annstase.hw3.domain.usecase.SendMessageUseCase
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_DRAFT = "KEY_DRAFT_MESSAGE"
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages
    private val errorTag = "ChatDetailViewModel"

    val draftMessage: MutableLiveData<String> =
        savedStateHandle.getLiveData(KEY_DRAFT, "")

    fun loadMessages(chatId: Int) = viewModelScope.launch {
        viewModelScope.launch {
            try {
                _messages.value = getMessagesUseCase(chatId)
            } catch (e: Exception) {
                Log.d(errorTag, "createChat", e)
            }
        }
    }

    fun onDraftChanged(text: String) {
        draftMessage.value = text
        savedStateHandle[KEY_DRAFT] = text
    }

    fun sendMessage(chatId: Int, text: String) {
        viewModelScope.launch {
            try {
                _messages.value = sendMessageUseCase(chatId, text)
                onDraftChanged("")
            } catch (e: Exception) {
                Log.d(errorTag, "sendMessage", e)
            }
        }
    }
}
