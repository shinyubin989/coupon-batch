package seoultech.app.batch.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.ItemReadListener
import org.springframework.stereotype.Component
import seoultech.app.batch.CouponProjection
import java.lang.Exception
import java.time.Duration
import java.time.LocalDateTime

@Component
class CouponItemReadListener : ItemReadListener<CouponProjection> {

    private val logger = KotlinLogging.logger {}

    private var start = LocalDateTime.now()
    private var end = LocalDateTime.now()

    override fun beforeRead() {
        start = LocalDateTime.now()
    }

    override fun afterRead(item: CouponProjection) {
        end = LocalDateTime.now()
        var duration = Duration.between(start,end).seconds
        logger.info { "${item.couponId}부터 시작한 ItemReader는 $duration 초가 걸렸다" }
    }

    override fun onReadError(ex: Exception) {
        println(ex.message)
        println(ex.cause)
    }
}