package com.beok.websocketsample.data

import javax.inject.Inject
import kotlinx.coroutines.channels.Channel

class WebSocketRepositoryImpl @Inject constructor(
    private val service: WebSocketService
) : WebSocketRepository {

    override fun event(): Result<Channel<SocketUpdate>> = runCatching {
        service.socketEventChannel
    }

    override fun stop(): Result<Unit> = runCatching {
        service.stop()
    }
}
