package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.Deck
import java.time.ZonedDateTime

data class DeckDto(val id: Int,
                   val name: String,
                   val date_creation: ZonedDateTime,
                   val date_update: ZonedDateTime,
                   val description_md: String,
                   val user_id: Int,
                   val investigator_code: String,
                   val investigator_name: String,
                   val slots: Map<String, Int>,
                   val ignoreDeckLimitSlots: Map<String, Int>?,
                   val version: String,
                   val xp: Int?,
                   val xp_adjustment: Int,
                   val exile_string: String?,
                   val taboo_id: String?,
                   val meta: String,
                   val tags: String,
                   val previous_deck: Int?,
                   val next_deck: Int?) {
    fun toDomain() = Deck(id, name, date_creation.toLocalDateTime(), date_update.toLocalDateTime(), description_md, user_id, investigator_code, investigator_name, slots,
            ignoreDeckLimitSlots, version, xp, xp_adjustment, exile_string, taboo_id, meta, tags, previous_deck, next_deck)
}
