package kz.adamant.domain.models

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Loading<T>(val localData: T?) : Resource<T>()
    class Error<T>(val message: String) : Resource<T>()
}