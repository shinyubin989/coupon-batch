package seoultech.app.batch.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.ItemWriteListener
import org.springframework.batch.item.Chunk
import org.springframework.stereotype.Component
import seoultech.app.batch.CouponProjection
import java.lang.Exception
import java.time.Duration
import java.time.LocalDateTime

@Component
class CouponItemWriterListener : ItemWriteListener<CouponProjection> {

    private val logger = KotlinLogging.logger {}

    private var start = LocalDateTime.now()
    private var end = LocalDateTime.now()

    override fun beforeWrite(items: Chunk<out CouponProjection>) {
        start = LocalDateTime.now()
    }

    override fun afterWrite(items: Chunk<out CouponProjection>) {
        end = LocalDateTime.now()
        var duration = Duration.between(start,end).seconds
        logger.info { "ItemWriter는 $duration 초가 걸렸다" }
    }
}