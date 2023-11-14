package seoultech.app.domain.coupon

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class ExpireBatchController(
        private val jobLauncher: JobLauncher,
        private val job: Job
) {

    @RequestMapping("/jobLauncher.html")
    @Throws(Exception::class)
    fun handle() {
        jobLauncher.run(job, JobParameters())
    }
}