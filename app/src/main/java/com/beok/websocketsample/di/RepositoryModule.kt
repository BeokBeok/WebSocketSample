package com.beok.websocketsample.di

import com.beok.websocketsample.data.WebSocketRepository
import com.beok.websocketsample.data.WebSocketRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsWebSocketRepository(impl: WebSocketRepositoryImpl): WebSocketRepository
}
