package com.beok.websocketsample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beok.websocketsample.data.WebSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WebSocketRepository
) : ViewModel() {

    private val _eventMessage = MutableSharedFlow<String>(
        replay = 2,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val eventMessage: SharedFlow<String> get() = _eventMessage.asSharedFlow()

    override fun onCleared() {
        repository.stop()
        super.onCleared()
    }

    fun observeEvent() = viewModelScope.launch {
        repository.event()
            .onSuccess { channel ->
                channel.consumeEach {
                    _eventMessage.emit(it.text)
                }
            }
            .onFailure {
                println()
            }
    }
}
