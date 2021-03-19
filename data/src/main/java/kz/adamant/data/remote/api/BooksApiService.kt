package kz.adamant.data.remote.api

import kz.adamant.data.remote.models.BookDto
import kz.adamant.data.remote.models.GenreDto
import kz.adamant.data.remote.models.ReadingBookDto
import kz.adamant.data.remote.models.RentInfoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BooksApiService {
    @GET("api/books")
    suspend fun allBooks(): Response<List<BookDto>>

    @GET("api/genres")
    suspend fun allGenres(): Response<List<GenreDto>>

    @GET("api/genres/{genreId}")
    suspend fun genreById(@Path("genreId") genreId: Int): Response<GenreDto?>

    @GET("api/rent")
    suspend fun allReadingBooks(): Response<List<ReadingBookDto>>

    @GET("api/rent/reading/{userId}")
    suspend fun userReadingBooks(@Path("userId") userId: String): Response<List<ReadingBookDto>>

    @POST("api/rent")
    suspend fun rentBook(@Body rentInfoDto: RentInfoDto): Response<ReadingBookDto>
}