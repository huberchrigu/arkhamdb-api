package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.*
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class CardProcessor(private val deckRepository: DeckRepository) : ItemProcessor<CardDto, Card> {
    private var decks: Sequence<Deck>? = null

    override fun process(card: CardDto): Card {
        if (decks == null) {
            decks = deckRepository.findAll().collectList().block()!!.asSequence()
        }
        val usages = decks!!.map { CardUsage(it.id, getUsages(it, card)) }.filter { it.usages > 0 }.toList()
        return card.toDomain(usages)
    }

    private fun getUsages(deck: Deck, card: CardDto) = deck.slots.getOrDefault(card.code, 0) + (if (deck.investigatorId == card.code) 1 else 0)
}
