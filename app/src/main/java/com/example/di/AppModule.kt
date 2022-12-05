package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.core.cryptography.CryptographyManager
import com.example.core.cryptography.CryptographyManagerImpl
import com.example.core.data.local.database.BookDao
import com.example.core.data.local.database.BookDatabase
import com.example.core.data.remote.BookApi
import com.example.core.domain.helpers.ThemeDatastore
import com.example.core.domain.repositories.BookRepository
import com.example.core.utils.Constants
import com.example.feature_book_details.domain.use_case.BookDetailsUseCases
import com.example.feature_book_details.domain.use_case.DeleteBookUseCase
import com.example.feature_book_details.domain.use_case.GetBookUseCase
import com.example.feature_book_details.domain.use_case.SaveBookUseCase
import com.example.feature_search_book.domain.useCase.BookListUseCases
import com.example.feature_search_book.domain.useCase.GetNewBooksUseCase
import com.example.feature_search_book.domain.useCase.SearchBookByNameUseCase
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
    fun provideRetrofitApi(): BookApi {
        return Retrofit.Builder().baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(BookApi::class.java)
    }


    @Provides
    @Singleton
    fun provideBookListUseCases(repository: BookRepository): BookListUseCases {
        return BookListUseCases(
            getNewBooksUseCase = GetNewBooksUseCase(repository = repository),
            searchBookByNameUseCase = SearchBookByNameUseCase(repository = repository)
        )
    }

    @Provides
    @Singleton
    fun provideBookDao(@ApplicationContext app: Context): BookDao {
        return Room.databaseBuilder(
            app, BookDatabase::class.java, Constants.DATABASE_NAME
        ).build().dao
    }

    @Provides
    @Singleton
    fun provideBookDetailsUseCases(
        repository: BookRepository
    ): BookDetailsUseCases {
        return BookDetailsUseCases(
            saveBook = SaveBookUseCase(repository = repository), getBookUseCase = GetBookUseCase(
                repository = repository
            ), deleteBookUseCase = DeleteBookUseCase(repository = repository)
        )
    }

    @Provides
    @Singleton
    fun provideThemeDataStore(@ApplicationContext context: Context): ThemeDatastore {
        return ThemeDatastore(context = context)
    }

    @Provides
    @Singleton
    fun provideCryptographyManager(): CryptographyManager {
        return CryptographyManagerImpl()
    }

}