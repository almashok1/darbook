package kz.adamant.bookstore.di

import kz.adamant.bookstore.utils.Constants
import kz.adamant.bookstore.viewmodels.AllBooksViewModel
import kz.adamant.bookstore.viewmodels.HomeViewModel
import kz.adamant.bookstore.viewmodels.SearchViewModel
import kz.adamant.data.local.BookDatabase
import kz.adamant.data.remote.api.BooksApiService
import kz.adamant.data.repository.BooksDataSource
import kz.adamant.data.repository.BooksRepositoryImpl
import kz.adamant.data.repository.datasources.BooksLocalDataSource
import kz.adamant.data.repository.datasources.BooksRemoteDataSource
import kz.adamant.domain.repository.BooksRepository
import kz.adamant.domain.usecases.GetAllBooksNewlyAddedUseCase
import kz.adamant.domain.usecases.GetAllBooksUseCase
import kz.adamant.domain.usecases.GetAllGenresUseCase
import kz.adamant.domain.usecases.GetAllReadingBooksUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val localDataSourceModule = module {
    single { BookDatabase.getInstance(androidContext()) }
    single<BooksDataSource>(named("local")) {
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

    single<BooksDataSource>(named("remote")) { BooksRemoteDataSource(get())  }
}

val repositoryModule = module {
    single<BooksRepository> {
        BooksRepositoryImpl(
            get(named("local")),
            get(named("remote")),
        )
    }
}

val useCasesModule = module {
    single { GetAllBooksUseCase(get()) }
    single { GetAllGenresUseCase(get()) }
    single { GetAllReadingBooksUseCase(get()) }
    single { GetAllBooksNewlyAddedUseCase(get()) }
}

val viewModelModule = module {
    viewModel<HomeViewModel> { HomeViewModel(get(), get()) }
    viewModel<AllBooksViewModel> { (booksType: Int) -> AllBooksViewModel(booksType, get(), get()) }
    viewModel<SearchViewModel> { SearchViewModel(get(), get()) }
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