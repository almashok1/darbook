package kz.adamant.domain.models

sealed class Resource<out T>(
    val data: T? = null,
){
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T?) : Resource<T>(data)
    class Error<T>(val throwable: Throwable, data: T? = null) : Resource<T>(data)
}