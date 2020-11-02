package ch.chrigu.arkhamdb.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class Deck(@Id val id: Int,
                val name: String,
                val dateCreation: LocalDateTime,
                val dateUpdate: LocalDateTime,
                val description: String,
                val userId: Int,
                val investigatorId: String,
                val investigatorName: String,
                val slots: Map<String, Int>,
                val ignoreDeckLimitSlots: Map<String, Int>?,
                val version: String,
                val xp: Int?,
                val xpAdjustment: Int,
                val exile: String?,
                val tabooId: String?,
                val meta: String,
                val tags: String,
                val previousDeck: Int?,
                val nextDeck: Int?)
