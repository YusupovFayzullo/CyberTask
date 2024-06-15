package uz.apphub.fayzullo.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**Created By Begzod Shokirov on 24/04/2024 **/

@Module
@InstallIn(SingletonComponent::class)
interface LocalModule {
//    @Binds
//    @Singleton
//    fun bindHomeRepository(homeRepository: HomeRepositoryImpl): HomeRepository
}