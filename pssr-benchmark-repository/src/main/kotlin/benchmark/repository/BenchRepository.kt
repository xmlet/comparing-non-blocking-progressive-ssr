package benchmark.repository

import io.reactivex.rxjava3.core.Observable

sealed interface BenchRepository<T : Any> {
    fun findAllReactive(): Observable<T>

    fun findAllIterable(): Iterable<T>
}
