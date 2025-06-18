package com.pera.sudoku.di

import androidx.room.Room
import com.pera.sudoku.model.ApiService
import com.pera.sudoku.model.SavedGameDao
import com.pera.sudoku.model.SudokuDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://sudoku-api.vercel.app/api/dosuku/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: android.content.Context): SudokuDatabase {
        return Room.databaseBuilder(
            context,
            SudokuDatabase::class.java,
            "sudoku_database"
        ).build()
    }

    @Provides
    fun provideSavedGameDao(db: SudokuDatabase): SavedGameDao{
        return db.savedGameDao()
    }
}
