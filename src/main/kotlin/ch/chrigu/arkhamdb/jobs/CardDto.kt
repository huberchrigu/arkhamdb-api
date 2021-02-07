package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.*

data class CardDto(
    val pack_code: String,
    val pack_name: String,
    val type_code: String,
    val type_name: String,
    val subtype_code: String?,
    val subtype_name: String?,
    val faction_code: String, val faction_name: String, val faction2_code: String?, val faction2_name: String?,
    val position: Int,
    val exceptional: Boolean,
    val myriad: Boolean,
    val code: String,
    val name: String,
    val real_name: String,
    val subname: String?,
    val cost: Int,
    val text: String?,
    val real_text: String,
    val quantity: Int,
    val skill_intellect: Int?, val skill_combat: Int?, val skill_willpower: Int?, val skill_agility: Int?,
    val xp: Int,
    val clues: Int?, val shroud: Int?,
    val clues_fixed: Boolean,
    val health: Int?, val sanity: Int?,
    val health_per_investigator: Boolean,
    val enemy_damage: Int?, val enemy_horror: Int?, val enemy_fight: Int?, val enemy_evade: Int?,
    val deck_limit: Int,
    val slot: String?, val real_slot: String?,
    val traits: String?,
    val real_traits: String?,
    val illustrator: String?,
    val is_unique: Boolean,
    val exile: Boolean,
    val hidden: Boolean,
    val permanent: Boolean,
    val double_sided: Boolean, val back_text: String?, val back_flavor: String?,
    val octgn_id: String?,
    val url: String,
    val imagesrc: String?,
    val linked_to_code: String?,
    val restrictions: Map<String, Map<String, String>>?, // ignore yet
    val encounter_position: Int?,
    val deck_requirements: Any?, // ignore yet
    val deck_options: Any?, // ignore yet
    val bonded_to: String?,
    val bonded_count: Int?
) {
    fun toDomain(usages: List<CardUsage>) = Card(
        code, CardText(name, real_name), subname, position, CardText(text ?: real_text, real_text), double_sided, back_text, back_flavor,
        quantity,
        listOfNotNull(CardAttribute(faction_name, faction_code), faction2_code?.let { CardAttribute(faction2_name!!, faction2_code) }),
        CardAttribute(type_name, type_code),
        getSubTypeOrNull(),
        real_traits?.let { CardText(traits ?: real_traits, real_traits) },
        BuildAttributes(xp, exceptional, myriad, deck_limit),
        PlayAttributes(getSlotOrNull(), cost, permanent, is_unique, exile), hidden, getSkillsOrNull(),
        Stats(sanity, health, health_per_investigator), getEnemyStatsOrNull(),
        LocationStats(clues, shroud, clues_fixed), CardAttribute(pack_name, pack_code),
        illustrator, octgn_id, url, imagesrc,
        usages,
        LinkedTo(linked_to_code),
        EncounterPosition(encounter_position),
        Bonded(bonded_to, bonded_count)
    )

    private fun getSlotOrNull() = if (slot != null && real_slot != null) CardText(slot, real_slot) else null

    private fun getEnemyStatsOrNull() = if (enemy_damage != null && enemy_horror != null && enemy_fight != null && enemy_evade != null)
        EnemyStats(enemy_damage, enemy_horror, enemy_fight, enemy_evade)
    else
        null

    private fun getSubTypeOrNull() = if (subtype_code != null && subtype_name != null) {
        CardAttribute(subtype_name, subtype_code)
    } else
        null

    private fun getSkillsOrNull() = if (skill_willpower != null && skill_intellect != null && skill_combat != null && skill_agility != null)
        Skills(skill_willpower, skill_intellect, skill_combat, skill_agility)
    else null
}
