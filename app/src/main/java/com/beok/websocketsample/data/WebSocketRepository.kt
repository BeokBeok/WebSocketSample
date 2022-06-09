package com.beok.websocketsample.data

import kotlinx.coroutines.channels.Channel

interface WebSocketRepository {

    fun event(): Result<Channel<SocketUpdate>>

    fun stop(): Result<Unit>
}
