package ch.chrigu.arkhamdb.domain

import org.springframework.data.annotation.Id

data class Card(
    @Id val code: String,
    val name: CardText,
    val subName: String?,
    val position: Int,

    val text: CardText,
    val doubleSided: Boolean,
    val backText: String?,
    val backFlavor: String?,
    val quantity: Int,

    val factions: List<CardAttribute>,
    val type: CardAttribute,
    val subtype: CardAttribute?,
    val traits: CardText?,

    val build: BuildAttributes,
    val play: PlayAttributes,

    val hidden: Boolean,

    val skills: Skills?,
    val stats: Stats,
    val enemyStats: EnemyStats?,
    val locationStatus: LocationStats,

    val pack: CardAttribute,
    val illustrator: String?,

    val octgnId: String?,
    val url: String,
    val imageSrc: String?,
    val usages: List<CardUsage>,

    val linkedTo: LinkedTo,
    val encounterPosition: EncounterPosition,
    val bonded: Bonded
)

data class CardText(val de: String, val en: String)

data class CardAttribute(val name: String, val code: String)
data class BuildAttributes(
    val xp: Int,
    val exceptional: Boolean,
    val myriad: Boolean,
    val deckLimit: Int
)

data class PlayAttributes(
    val slot: CardText?,
    val cost: Int,
    val permanent: Boolean,
    val unique: Boolean,
    val exile: Boolean
)

data class Skills(
    val willpower: Int,
    val intellect: Int,
    val combat: Int,
    val agility: Int
)

data class Stats(
    val sanity: Int?,
    val health: Int?,
    val healthPerInvestigator: Boolean
)

data class EnemyStats(
    val damage: Int,
    val horror: Int,
    val fight: Int,
    val evade: Int
)

data class LocationStats(
    val clues: Int?,
    val shroud: Int?,
    val cluesFixed: Boolean
)

data class CardUsage(val deckId: Int, val usages: Int)

data class LinkedTo(val code: String?)
data class EncounterPosition(val position: Int?)
data class Bonded(val to: String?, val count: Int?)
