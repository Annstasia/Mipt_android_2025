package ru.mipt.annstase.hw3.viewmodel


import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mipt.annstase.hw3.domain.model.Chat
import ru.mipt.annstase.hw3.domain.usecase.CreateChatUseCase
import ru.mipt.annstase.hw3.domain.usecase.GetChatsUseCase
import javax.inject.Inject

@HiltViewModel
class AllChatsViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {
    private val errorTag = "AllChatsViewModel"
    private val _chats = MutableLiveData<List<Chat>>()
    val chats: LiveData<List<Chat>> = _chats

    init {
        loadChats()
    }

    fun createChat(name: String) {
        viewModelScope.launch {
            try {
                _chats.value = createChatUseCase(name)
            } catch (e: Exception) {
                Log.d(errorTag, "createChat", e)
            }
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            try {
                val list = getChatsUseCase()
                _chats.value = list
            } catch (e: Exception) {

                Log.d(errorTag, "loadChats", e)
            }
        }
    }
}
