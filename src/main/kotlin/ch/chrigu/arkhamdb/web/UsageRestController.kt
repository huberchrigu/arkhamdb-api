package ch.chrigu.arkhamdb.web

import ch.chrigu.arkhamdb.domain.*
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * * /usage: All cards
 * * /usage/_search: Consider decks by investigator (optional), and cards by pack (*notPack*, optional) and update date (*updatedSince*).
 * * /usage/factions: Card usage by factions
 * * /usage/packs: Card usage by packs
 *
 * Weight = The number of (filtered by investigator) decks (update date) since the card's pack is available.
 */
@RestController
@RequestMapping("/usage")
class UsageRestController(private val cardRepository: CardRepository, private val deckRepository: DeckRepository, private val packRepository: PackRepository) {
    @GetMapping
    fun getCardUsage(): Flux<CardUsageDto> {
        return getPlayerCards()
                .flatMap { toCardUsageOverAllDecks(it, countUsage(it)) }
                .sort()
    }

    @GetMapping("/_search")
    fun findCards(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) updatedSince: LocalDate?,
                  @RequestParam notPack: String?, @RequestParam investigator: String?): Flux<CardUsageDto> {
        val after = updatedSince?.atTime(0, 0) ?: LocalDateTime.now().minusMonths(1)
        return deckRepository.findByDateUpdateAfter(after)
                .filter { investigator == null || it.investigatorId == investigator }
                .collectList()
                .map { decks ->
                    decks.map { deck -> deck.slots + Pair(deck.investigatorId, 1) }
                            .flatMap { it.entries }
                            .groupBy({ it.key }, { it.value })
                            .mapValues { (_, value) -> value.fold(0) { a, b -> a + b } }
                            .map { (key, value) ->
                                cardRepository.findById(key)
                                        .filter { card -> notPack == null || (card.pack.name != notPack) }
                                        .flatMap { card -> toCardUsageOverGivenDecks(card, value, decks) }
                            }.toFlux().flatMap { it }
                }.flatMapMany { it }
                .sort()
    }

    @GetMapping("/factions")
    fun getFactions() = getPlayerCards().flatMap { it.factions.map { faction -> Pair(faction.name, countUsage(it)) }.toFlux() }
            .collectList().map { groupByKey(it) }

    @GetMapping("/packs")
    fun getPacks() = packRepository.findAll().flatMap { pack ->
        getPlayerCards().filter { it.pack.code == pack.code }.count()
                .map { UsageDto(pack.name, it.toInt(), pack.decksSincePack) }
    }.collectList().map { it.sorted() }

    private fun groupByKey(list: List<Pair<String, Int>>): List<UsageDto> {
        return list.groupBy({ it.first }) { it.second }.mapValues { it.value.fold(0) { a, b -> a + b } }
                .map { UsageDto(it.key, it.value, 1) }
                .sorted()
    }

    private fun getPlayerCards() = cardRepository.findAll().filter { card -> !card.factions.all { it.code == "mythos" } }

    private fun toCardUsageOverAllDecks(card: Card, numUsed: Int) = packRepository.findById(card.pack.code)
            .map { toCardUsage(card, numUsed, it.decksSincePack) }

    private fun toCardUsageOverGivenDecks(card: Card, numUsed: Int, decks: List<Deck>) = packRepository.findById(card.pack.code).map { pack ->
        decks.filter { it.dateUpdate.isAfter(pack.available.atStartOfDay()) }.count()
    }.map { toCardUsage(card, numUsed, it) }

    private fun toCardUsage(card: Card, numUsed: Int, weight: Int) =
            CardUsageDto(card.code, card.name.de, card.build.xp, card.factions.map { it.name }, numUsed, weight, card.pack.name)


    private fun countUsage(card: Card) = card.usages.map { it.usages }.fold(0) { a, b -> a + b }

    class CardUsageDto(val code: String, name: String, val xp: Int, val factions: List<String>, numUsed: Int, weight: Int, val pack: String) : UsageDto(name, numUsed, weight)

    open class UsageDto(val name: String, val numUsed: Int, val weight: Int) : Comparable<UsageDto> {
        val usedWeighted: BigDecimal = numUsed.toBigDecimal().divide(weight.toBigDecimal(), 5, RoundingMode.HALF_UP)

        override fun compareTo(other: UsageDto): Int {
            return other.usedWeighted.compareTo(usedWeighted)
        }
    }
}
