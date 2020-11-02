package ch.chrigu.arkhamdb.web

import ch.chrigu.arkhamdb.jobs.ImportDecksJobConfiguration
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JobRestController(private val jobLauncher: JobLauncher, private val insertionJob: Job,
                        private val jobRepository: JobRepository) {

    @GetMapping("/sync")
    fun sync(): JobExecution {
        return jobLauncher.run(insertionJob, JobParameters())
    }

    @GetMapping("/status")
    fun status() = getLastJobExecution().status

    private fun getLastJobExecution() = jobRepository.getLastJobExecution(ImportDecksJobConfiguration.NAME, JobParameters())!!
}
