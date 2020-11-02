package ch.chrigu.arkhamdb.jobs

import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.util.concurrent.atomic.AtomicInteger

@Component
class CardReader(private val webClient: WebClient) : ItemReader<CardDto> {
    private var all: List<CardDto>? = null
    private var idx = AtomicInteger(0)

    override fun read(): CardDto? {
        if (all == null) {
            all = webClient.get().uri("/api/public/cards/").retrieve().bodyToFlux(CardDto::class.java).collectList().block()
        }
        return all!!.getOrNull(idx.getAndIncrement())
    }
}
