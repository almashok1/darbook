package kz.adamant.bookstore

import android.app.Application
import kz.adamant.data.local.BookDatabase
import kz.adamant.data.remote.RetrofitBuilder
import kz.adamant.data.remote.api.BooksApiService
import kz.adamant.data.repository.BooksRepositoryImpl
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.domain.usecases.GetAllBooksUseCase
import kz.adamant.domain.usecases.GetAllGenresUseCase

class App: Application() {
    private val database by lazy { BookDatabase.getInstance(this) }

    private val booksLocalDataSource by lazy { BooksLocalDataSource(database.booksDao(), database.genresDao()) }

    private val apiService by lazy { RetrofitBuilder.getInstance().create(BooksApiService::class.java) }

    private val booksRemoteDataSource by lazy { BooksRemoteDataSource(apiService) }

    private val booksRepository by lazy {
        BooksRepositoryImpl(
            localDataSource = booksLocalDataSource,
            remoteDataSource = booksRemoteDataSource
        )
    }


    val getAllBooksUseCase by lazy { GetAllBooksUseCase(booksRepository) }

    val getAllGenresUseCase by lazy { GetAllGenresUseCase(booksRepository) }
}