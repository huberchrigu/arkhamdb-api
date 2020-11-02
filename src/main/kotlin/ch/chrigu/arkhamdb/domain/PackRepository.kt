package ch.chrigu.arkhamdb.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PackRepository : ReactiveMongoRepository<Pack, String>
