package ch.chrigu.arkhamdb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ArkhamDbApplication

fun main(args: Array<String>) {
    runApplication<ArkhamDbApplication>(*args)
}
