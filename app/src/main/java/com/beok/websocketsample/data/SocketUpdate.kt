package com.beok.websocketsample.data

data class SocketUpdate(
    val text: String = "",
    val exception: Throwable? = null
)
