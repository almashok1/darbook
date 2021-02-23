package kz.adamant.bookstore

import android.app.Application
import kz.adamant.bookstore.data.local.BookDatabase
import kz.adamant.bookstore.data.repository.BooksRepositoryIml
import kz.adamant.bookstore.data.repository.datasources.BooksLocalDataSource
import kz.adamant.bookstore.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.bookstore.data.repository.datasources.mappers.BookEntityToBookMapper

class App: Application() {
    private val database by lazy { BookDatabase.getInstance(this) }

    private val booksLocalDataSource by lazy { BooksLocalDataSource(database.booksDao()) }

    // blank data source
    private val booksRemoteDataSource by lazy { BooksRemoteDataSource() }

    private val booksMapper by lazy { BookEntityToBookMapper() }

    val booksRepository by lazy {
        BooksRepositoryIml(
            localDataSource = booksLocalDataSource,
            remoteDataSource = booksRemoteDataSource,
            mapper = booksMapper
        )
    }
}