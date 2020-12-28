package com.example.covidmind

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.covidmind.repos.LocalDatabase
import com.example.covidmind.repos.MoodNotesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@HiltAndroidApp
class CovidMindApplication : Application() {}

@Module
@InstallIn(ApplicationComponent::class)
object ProductionModules {
    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext appContext: Context): LocalDatabase {
        return Room
            .databaseBuilder(appContext, LocalDatabase::class.java, "covid_mind.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMoodNotesRepository(
        localDatabase: LocalDatabase
    ): MoodNotesRepository = MoodNotesRepository(localDatabase)
}