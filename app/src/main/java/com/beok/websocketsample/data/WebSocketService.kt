package com.beok.websocketsample.data

import com.beok.websocketsample.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketService(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope = CoroutineScope(ioDispatcher),
    val socketEventChannel: Channel<SocketUpdate> = Channel(10)
) : WebSocketListener() {

    private lateinit var webSocket: WebSocket

    override fun onOpen(webSocket: WebSocket, response: Response) {
        this.webSocket = webSocket
        webSocket.send("{\"type\":\"ticker\", \"symbols\": [\"BTC_KRW\"], \"tickTypes\": [\"30M\"]}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        coroutineScope.launch {
            socketEventChannel.send(SocketUpdate(text = text))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        coroutineScope.launch {
            socketEventChannel.send(SocketUpdate(exception = IllegalStateException(reason)))
            socketEventChannel.close()

            webSocket.cancel()
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        coroutineScope.launch {
            socketEventChannel.send(SocketUpdate(exception = t))
        }
    }

    fun stop() {
        socketEventChannel.cancel()
        socketEventChannel.close()
        webSocket.cancel()
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1_000
    }
}
