package kz.adamant.data.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kz.adamant.domain.models.Resource

suspend inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: suspend () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    emit(Resource.Loading(null))
    val data = query().first()

    val flow: Flow<Resource<ResultType>> =
        if (shouldFetch(data)) {
            emit(Resource.Loading(data))

            try {
                saveFetchResult(fetch())
                query().map { Resource.Success(it) }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                query().map { Resource.Error(throwable.localizedMessage ?: "") }
            }
        } else {
            query().map { Resource.Success(it) }
        }

    emitAll(flow)
}.flowOn(Dispatchers.IO)