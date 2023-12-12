package seoultech.app.domain.coupon

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import seoultech.app.batch.CouponProjection
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class JdbcCouponUpdater(private val jdbcTemplate: JdbcTemplate) {
    fun save(coupon: CouponProjection) {
        jdbcTemplate.update("update coupon set expired = true where id = ?", coupon.couponId)
    }

    fun saveAll(coupons: List<CouponProjection>) {
        val sql = "update coupon set expired = true where id = ?"
        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            override fun setValues(ps: PreparedStatement, i: Int) {
                val coupon = coupons[i]
                ps.setLong(1, coupon.couponId)
            }

            override fun getBatchSize(): Int {
                return coupons.size
            }
        })
    }
}

