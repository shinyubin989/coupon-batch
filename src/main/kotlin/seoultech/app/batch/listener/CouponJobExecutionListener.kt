package seoultech.app.batch.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class CouponJobExecutionListener : JobExecutionListener {

    private val logger = KotlinLogging.logger {}

    override fun beforeJob(jobExecution: JobExecution) {
        val name = jobExecution.jobInstance.jobName
        logger.info { "job name $name start" }
    }

    override fun afterJob(jobExecution: JobExecution) {
        val jobName = jobExecution.jobInstance.jobName
        val startTime = jobExecution.startTime
        val endTime = jobExecution.endTime
        val executionTime = Duration.between(startTime, endTime)
        logger.info { "job name $jobName end. execution time is $executionTime" }
    }
}