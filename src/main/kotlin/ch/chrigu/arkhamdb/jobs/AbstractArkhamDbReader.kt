package ch.chrigu.arkhamdb.jobs

import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemReader
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.concurrent.atomic.AtomicInteger

abstract class AbstractArkhamDbReader<T>(private val webClient: WebClient, private val repository: ReactiveMongoRepository<*, Int>, private val clazz: Class<T>) : ItemReader<T> {
    private var nextDeckId: AtomicInteger? = null
    private var failed = AtomicInteger(0)
    private var logger = LoggerFactory.getLogger(AbstractArkhamDbReader::class.java)

    override fun read(): T? {
        return getNext().block()
    }

    @Synchronized
    private fun getNext(): Mono<T> {
        if (nextDeckId == null) {
            nextDeckId = repository.count().map { AtomicInteger(it.toInt()) }.block()!!
        }
        val next = nextDeckId!!.incrementAndGet()
        return if (failed.get() < 50)
            webClient.get().uri(getUri(next)).exchange()
                    .flatMap { response -> toDto(response) }
                    .doOnNext { logger.info("Read ${clazz.simpleName} ${getId(it)}") }
                    .switchIfEmpty { getNext() }
        else
            Mono.empty()
    }

    abstract fun getId(dto: T): String

    abstract fun getUri(id: Int): String

    private fun toDto(response: ClientResponse): Mono<T> {
        if (response.headers().contentType().filter { it.isCompatibleWith(MediaType.APPLICATION_JSON) }.isPresent) {
            failed.set(0)
            return response.bodyToMono(clazz)
        } else {
            failed.incrementAndGet()
            return Mono.empty()
        }
    }
}
