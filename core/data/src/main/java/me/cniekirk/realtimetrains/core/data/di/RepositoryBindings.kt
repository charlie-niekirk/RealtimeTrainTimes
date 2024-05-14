package me.cniekirk.realtimetrains.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.cniekirk.realtimetrains.core.data.repository.CrsRepository
import me.cniekirk.realtimetrains.core.data.repository.CrsRepositoryImpl
import me.cniekirk.realtimetrains.core.data.repository.RealtimeTrainsRepository
import me.cniekirk.realtimetrains.core.data.repository.RealtimeTrainsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryBindings {

    @Binds
    fun bindsRealtimeTrainsRepository(realtimeTrainsRepositoryImpl: RealtimeTrainsRepositoryImpl): RealtimeTrainsRepository

    @Binds
    fun bindsCrsRepository(crsRepositoryImpl: CrsRepositoryImpl): CrsRepository
}