package uz.apphub.fayzullo.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.apphub.fayzullo.data.room.AppDatabase
import uz.apphub.fayzullo.data.room.dao.SignatureDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase = Room.databaseBuilder(
        appContext, AppDatabase::class.java, "app_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideCardDao(database: AppDatabase): SignatureDao {
        return database.cardDao()
    }
}