package ch.chrigu.arkhamdb.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(webClientBuilder: WebClient.Builder) = webClientBuilder.baseUrl("https://de.arkhamdb.com").build()
}
