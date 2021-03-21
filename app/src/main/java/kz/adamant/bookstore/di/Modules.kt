package kz.adamant.bookstore.di

import kz.adamant.bookstore.utils.Constants
import kz.adamant.bookstore.viewmodels.BookDetailViewModel
import kz.adamant.bookstore.viewmodels.BookScannerViewModel
import kz.adamant.bookstore.viewmodels.HomeViewModel
import kz.adamant.bookstore.viewmodels.SearchViewModel
import kz.adamant.data.local.BookDatabase
import kz.adamant.data.remote.api.BooksApiService
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.data.repository.BooksRepositoryImpl
import kz.adamant.data.repository.GenreRepositoryImpl
import kz.adamant.data.repository.RentRepositoryImpl
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.domain.repository.BooksRepository
import kz.adamant.domain.repository.GenreRepository
import kz.adamant.domain.repository.RentRepository
import kz.adamant.domain.usecases.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val localDataSourceModule = module {
    single { BookDatabase.getInstance(androidContext()) }
    single {
        BooksLocalDataSource(
            get<BookDatabase>().booksDao(),
            get<BookDatabase>().genresDao(),
            get<BookDatabase>().readingBooksDao()
        )
    }
}


val remoteDataSourceModule = module {
    single { GsonConverterFactory.create() }
    single{ provideRetrofit(get(), Constants.BASE_URL) }
    single<BooksApiService> { get<Retrofit>().create(BooksApiService::class.java) }

    single { BooksRemoteDataSource(get())  }
}

val repositoryModule = module {
    single<BooksRepository> {
        BooksRepositoryImpl(
            get<BooksLocalDataSource>(),
            get<BooksRemoteDataSource>(),
        )
    }
    single<RentRepository> {
        RentRepositoryImpl(get<BooksLocalDataSource>(), get<BooksRemoteDataSource>())
    }

    single<GenreRepository> {
        GenreRepositoryImpl(
            get<BooksLocalDataSource>(),
            get<BooksRemoteDataSource>(),
        )
    }
}

val useCasesModule = module {
    single { GetAllBooksUseCase(get()) }
    single { GetAllGenresUseCase(get()) }
    single { GetReadingBooksUseCase(get()) }
    single { GetAllBooksNewlyAddedUseCase(get()) }
    single { GetBookRentingMarkUseCase(get()) }
    single { GetGenreUseCase(get()) }
    single { RentBookUseCase(get()) }
    single { GetSingleBookUseCase(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { BookDetailViewModel(get(), get()) }
    viewModel { BookScannerViewModel(get(), get()) }
}



val modules = listOf(
    localDataSourceModule,
    remoteDataSourceModule,
    repositoryModule,
    useCasesModule,
    viewModelModule,
)

fun provideRetrofit(gsonConverterFactory: GsonConverterFactory, baseUrl: String): Retrofit {
    return synchronized(Any()) {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .build()
    }
}