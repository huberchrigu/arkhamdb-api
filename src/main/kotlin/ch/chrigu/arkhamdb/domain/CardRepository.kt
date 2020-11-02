package ch.chrigu.arkhamdb.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CardRepository : ReactiveMongoRepository<Card, String>
