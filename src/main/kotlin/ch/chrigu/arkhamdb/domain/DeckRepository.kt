package ch.chrigu.arkhamdb.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import java.time.LocalDateTime

interface DeckRepository : ReactiveMongoRepository<Deck, Int> {
    fun findByDateUpdateAfter(updatedSince: LocalDateTime): Flux<Deck>
}
