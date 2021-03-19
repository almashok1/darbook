package kz.adamant.bookstore.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kz.adamant.domain.models.Resource
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun <Domain, Dvo> Flow<Resource<List<Domain>>>.asLiveDataMapped(
    context: CoroutineContext = EmptyCoroutineContext,
    timeoutInMs: Long = 10L,
    mapper: (Resource<List<Domain>>) -> Resource<List<Dvo>>
): LiveData<Resource<List<Dvo>>> {
    return liveData(context, timeoutInMs) {
        collect {
            emit(mapper(it))
        }
    }
}