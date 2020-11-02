package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.Deck
import ch.chrigu.arkhamdb.domain.DeckRepository
import ch.chrigu.arkhamdb.domain.Pack
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class PackProcessor(private val deckRepository: DeckRepository) : ItemProcessor<PackDto, Pack> {
    private var decks: Sequence<Deck>? = null

    override fun process(pack: PackDto): Pack {
        if (decks == null) {
            decks = deckRepository.findAll().collectList().block()!!.asSequence()
        }
        val decksSincePack = decks!!.filter { it.dateUpdate.isAfter(pack.getAvailableOrNow().atStartOfDay()) }.count()
        return pack.toDomain(decksSincePack)
    }
}
