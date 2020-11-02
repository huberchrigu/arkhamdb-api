package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.DeckRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux

@Component
class DeckWriter(private val deckRepository: DeckRepository) : ItemWriter<DeckDto> {
    private val logger = LoggerFactory.getLogger(DeckWriter::class.java)

    override fun write(deck: List<DeckDto>) {
        deck.toFlux()
                .map { it.toDomain() }
                .flatMap { deckRepository.save(it) }
                .doOnNext { logger.info("Saved deck ${it.id}") }
                .subscribe()
    }
}
