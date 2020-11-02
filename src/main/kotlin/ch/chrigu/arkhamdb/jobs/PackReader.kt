package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.Pack
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

@Component
class PackReader(private val webClient: WebClient) : ItemReader<PackDto> {
    private var all: List<PackDto>? = null
    private var idx = AtomicInteger(0)

    override fun read(): PackDto? {
        if (all == null) {
            all = webClient.get().uri("/api/public/packs/").retrieve().bodyToFlux(PackDto::class.java).collectList().block()!!
        }
        return all!!.getOrNull(idx.getAndIncrement())
    }
}

data class PackDto(val code: String, val name: String, val available: LocalDate?) {
    fun toDomain(decksSincePack: Int) = Pack(code, name, getAvailableOrNow(), decksSincePack)

    @JsonIgnore
    fun getAvailableOrNow() = available ?: LocalDate.now()
}
