package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.Pack
import ch.chrigu.arkhamdb.domain.PackRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux

@Component
class PackWriter(private val packRepository: PackRepository) : ItemWriter<Pack> {
    private val logger = LoggerFactory.getLogger(PackWriter::class.java)

    override fun write(packs: List<Pack>) {
        packs.toFlux()
                .flatMap { packRepository.save(it) }
                .doOnNext { logger.info("Saved card ${it.code}") }
                .subscribe()
    }
}
