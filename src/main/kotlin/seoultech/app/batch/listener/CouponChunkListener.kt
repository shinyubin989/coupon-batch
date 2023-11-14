package seoultech.app.batch.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.ChunkListener
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class CouponChunkListener : ChunkListener {

    private val logger = KotlinLogging.logger {}
    private var start = LocalDateTime.now()
    private var end = LocalDateTime.now()

    override fun beforeChunk(context: ChunkContext) {
//        val readCount = context.stepContext.stepExecution.readCount
//        logger.info { "before chunk. read count is $readCount" }
        start = LocalDateTime.now()
    }

    override fun afterChunk(context: ChunkContext) {
//        val startTime = context.stepContext.stepExecution.startTime
//        val endTime = context.stepContext.stepExecution.endTime
////        val executionTime = Duration.between(startTime, endTime)
//        val commitCount = context.stepContext.stepExecution.commitCount
//
//        logger.info { "after chunk. commit count is $commitCount" }
        end = LocalDateTime.now()
        var duration = Duration.between(start,end).seconds
        logger.info { "Chunk는 $duration 초가 걸렸다" }
    }
}