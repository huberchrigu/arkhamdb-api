package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.DeckRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class DeckReader(webClient: WebClient, deckRepository: DeckRepository) : AbstractArkhamDbReader<DeckDto>(webClient, deckRepository, DeckDto::class.java) {
    override fun getId(dto: DeckDto): String {
        return dto.id.toString()
    }

    override fun getUri(id: Int): String {
        return "/api/public/decklist/$id"
    }
}
