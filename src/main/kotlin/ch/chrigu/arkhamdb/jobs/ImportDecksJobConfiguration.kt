package ch.chrigu.arkhamdb.jobs

import ch.chrigu.arkhamdb.domain.Card
import ch.chrigu.arkhamdb.domain.Pack
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableBatchProcessing
@Configuration
class ImportDecksJobConfiguration(private val jobBuilderFactory: JobBuilderFactory, private val stepBuilderFactory: StepBuilderFactory) {

    @Bean
    fun importDecksStep(deckReader: DeckReader, deckWriter: DeckWriter): Step {
        return stepBuilderFactory["importDecksStep"]
                .chunk<DeckDto, DeckDto>(10)
                .reader(deckReader)
                .writer(deckWriter)
                .build()
    }


    @Bean
    fun importCardsStep(cardReader: CardReader, cardProcessor: CardProcessor, cardWriter: CardWriter): Step {
        return stepBuilderFactory["importCardsStep"]
                .chunk<CardDto, Card>(10)
                .reader(cardReader)
                .processor(cardProcessor)
                .writer(cardWriter)
                .build()
    }


    @Bean
    fun importPacksStep(packReader: PackReader, packProcessor: PackProcessor, packWriter: PackWriter): Step {
        return stepBuilderFactory["importPacksStep"]
                .chunk<PackDto, Pack>(10)
                .reader(packReader)
                .processor(packProcessor)
                .writer(packWriter)
                .build()
    }

    @Bean
    fun importDecksJob(importDecksStep: Step, importCardsStep: Step, importPacksStep: Step): Job {
        return jobBuilderFactory[NAME]
                .start(importDecksStep)
                .next(importCardsStep)
                .next(importPacksStep)
                .build()
    }

    companion object {
        const val NAME = "importDecksJob"
    }
}
