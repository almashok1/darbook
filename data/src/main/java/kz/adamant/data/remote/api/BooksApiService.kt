package kz.adamant.data.remote.api

import kz.adamant.data.remote.models.BookDto
import kz.adamant.data.remote.models.GenreDto
import retrofit2.Response
import retrofit2.http.GET

interface BooksApiService {
    @GET("api/books")
    suspend fun allBooks(): Response<List<BookDto>>

    @GET("api/genres")
    suspend fun allGenres(): Response<List<GenreDto>>
}