package kz.adamant.domain.models

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Loading<T>(val localData: T?) : Resource<T>()
    class Error<T>(val throwable: Throwable, val data: T? = null) : Resource<T>()
}