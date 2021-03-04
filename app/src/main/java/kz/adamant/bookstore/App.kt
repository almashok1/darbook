package kz.adamant.bookstore

import android.annotation.SuppressLint
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


    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
//        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
//        connectivityManager?.registerDefaultNetworkCallback(object :
//            ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                NetworkConnectivity.isNetworkAvailable = true
//                super.onAvailable(network)
//                Log.d("TAG", "onAvailable: true")
//            }
//
//            override fun onLost(network: Network) {
//                NetworkConnectivity.isNetworkAvailable = false
//                super.onLost(network)
//                Log.d("TAG", "onLost: false")
//            }
//        })

    }
}