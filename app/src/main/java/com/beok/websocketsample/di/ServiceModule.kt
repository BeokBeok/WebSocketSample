package com.beok.websocketsample.di

import com.beok.websocketsample.data.WebSocketService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.Request

@Module
@InstallIn(ViewModelComponent::class)
object ServiceModule {

    @Provides
    fun providesWebSocketService(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        client: OkHttpClient,
        request: Request
    ): WebSocketService = WebSocketService(ioDispatcher).also {
        client.newWebSocket(request, it)
    }
}
