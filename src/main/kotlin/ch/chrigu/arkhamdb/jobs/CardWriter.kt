package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.Card
import ch.chrigu.arkhamdb.domain.CardRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux

@Component
class CardWriter(private val cardRepository: CardRepository) : ItemWriter<Card> {
    private val logger = LoggerFactory.getLogger(CardWriter::class.java)

    override fun write(cards: List<Card>) {
        cards.toFlux()
                .flatMap { cardRepository.save(it) }
                .doOnNext { logger.info("Saved card ${it.code}") }
                .subscribe()
    }
}
