package kz.adamant.data.remote.api

import kz.adamant.data.remote.models.BookDto
import retrofit2.Response
import retrofit2.http.GET

interface BooksApiService {
    @GET("api/books")
    suspend fun allBooks(): Response<List<BookDto>>
}