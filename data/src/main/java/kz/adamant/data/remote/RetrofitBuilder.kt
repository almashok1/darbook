package kz.adamant.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://dar-library.dar-dev.zone/"

    private var INSTANCE: Retrofit? = null

    private val lock = Any()

    fun getInstance(): Retrofit {
        synchronized(lock) {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return INSTANCE!!
        }
    }
}