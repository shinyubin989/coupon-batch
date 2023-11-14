package seoultech.app.batch.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.stereotype.Component

@Component
class CouponStepExecutionListener : StepExecutionListener {

    private val logger = KotlinLogging.logger {}

    override fun beforeStep(stepExecution: StepExecution) {
        val stepName = stepExecution.stepName
        logger.info { "step name $stepName start" }
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        val stepName = stepExecution.stepName
        val exitStatus = stepExecution.exitStatus
        logger.info { "stepName $stepName end. exitStatus is $exitStatus" }
        return null
    }
}