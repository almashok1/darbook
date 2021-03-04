package kz.adamant.domain.utils

inline fun <T, R,> Iterable<T>.filterThenMap(predicate: (T) -> Boolean, transform: (T) -> R): List<R> {
    return filterThenMap(ArrayList<R>(), predicate, transform)
}
inline fun <T, R, C: MutableCollection<in R>>
        Iterable<T>.filterThenMap(collection: C, predicate: (T) -> Boolean,
                                  transform: (T) -> R): C {
    for(element in this) {
        if (predicate(element)) {
            collection.add(transform(element))
        }
    }
    return collection
}