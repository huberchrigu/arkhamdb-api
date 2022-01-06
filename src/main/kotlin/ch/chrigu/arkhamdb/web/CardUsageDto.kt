package ch.chrigu.arkhamdb.web

class CardUsageDto(val code: String, name: String, val subName: String?, val xp: Int, val factions: List<String>, numUsed: Int, weight: Int, val pack: String,
val type: String, val subType: String?, val bondedTo: String?) :
    UsageRestController.UsageDto(name, numUsed, weight)
