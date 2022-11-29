package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.local.database.BookDatabase
import com.example.core.data.remote.BookApi
import com.example.core.data.repositories.BookApiRepositoryImpl
import com.example.core.data.repositories.SavedBooksRepositoryImpl
import com.example.core.domain.repositories.BookApiRepository
import com.example.core.domain.repositories.SavedBooksRepository
import com.example.core.presentation.BookApp
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

    /* @Provides
     @Singleton
     fun provideApp(app: BookApp): BookApp {
         return app
     }*/

    @Provides
    @Singleton
    fun provideRetrofitApi(): BookApi {
        return Retrofit.Builder().baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(BookApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBookRepository(api: BookApi): BookApiRepository {
        return BookApiRepositoryImpl(api = api)
    }

    @Provides
    @Singleton
    fun provideBookListUseCases(repository: BookApiRepository): BookListUseCases{
        return BookListUseCases(
            getNewBooksUseCase = GetNewBooksUseCase(repository = repository),
            searchBookByNameUseCase = SearchBookByNameUseCase(repository = repository)
        )
    }

    @Provides
    @Singleton
    fun provideBookDatabase(@ApplicationContext app: Context): BookDatabase {
        return Room.databaseBuilder(
            app, BookDatabase::class.java, Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSavedBookRepository(bookDatabase: BookDatabase): SavedBooksRepository {
        return SavedBooksRepositoryImpl(bookDatabase.dao)
    }

    @Provides
    @Singleton
    fun provideBookDetailsUseCases(
        savedBooksRepository: SavedBooksRepository, bookApiRepository: BookApiRepository
    ): BookDetailsUseCases {
        return BookDetailsUseCases(
            saveBook = SaveBookUseCase(savedBooksRepository = savedBooksRepository),
            getBookUseCase = GetBookUseCase(
                savedBooksRepository = savedBooksRepository, bookApiRepository = bookApiRepository
            ),
            deleteBookUseCase = DeleteBookUseCase(savedBooksRepository = savedBooksRepository)
        )
    }
}