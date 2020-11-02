package ch.chrigu.arkhamdb.domain

import org.springframework.data.annotation.Id
import java.time.LocalDate

data class Pack(@Id val code: String, val name: String, val available: LocalDate, val decksSincePack: Int)
