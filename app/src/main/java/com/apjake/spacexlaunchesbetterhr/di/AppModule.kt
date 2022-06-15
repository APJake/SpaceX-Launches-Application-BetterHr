package com.apjake.spacexlaunchesbetterhr.di

import com.apjake.spacexlaunchesbetterhr.BuildConfig
import com.apjake.spacexlaunchesbetterhr.data.repository.SpaceXRepositoryImpl
import com.apjake.spacexlaunchesbetterhr.domain.repository.SpaceXRepository
import com.apjake.spacexlaunchesbetterhr.domain.usecase.GetLaunchDetailUseCase
import com.apollographql.apollo3.ApolloClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindSpaceXRepository(
        repository: SpaceXRepositoryImpl
    ): SpaceXRepository

    @Module
    @InstallIn(SingletonComponent::class)
    object Provide{
        @Provides
        @Singleton
        fun provideApolloClient(): ApolloClient{
            return ApolloClient.Builder()
                .serverUrl(BuildConfig.SERVER_URL)
                .build()
        }
    }

//    @InstallIn(FragmentComponent::class)
//    @AssistedModule
//    @Module
//    interface AssistedInjectModule {}


}