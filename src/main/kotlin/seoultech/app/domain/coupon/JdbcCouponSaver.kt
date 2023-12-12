package seoultech.app.domain.coupon

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import seoultech.app.batch.CouponProjection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.*

@Repository
class JdbcCouponSaver(private val jdbcTemplate: JdbcTemplate){

    fun save(coupon: CouponProjection) {
        jdbcTemplate.update(
                "INSERT INTO notification (id, coupon_id, created_at, updated_at, user_id, content, title) VALUES (?, ?, now(), now(), ?, ?, ?)",
                UUID.randomUUID().toString(),
                coupon.couponId,
                coupon.userId,
                "${coupon.name} 쿠폰이 만료되었습니다.",
                "${coupon.name} 쿠폰이 만료되어 만료 쿠폰함으로 이동하였습니다."
                )
    }

    fun saveAll(coupons: List<CouponProjection>) {
        val sql = "INSERT INTO notification (id, coupon_id, created_at, updated_at, user_id, content, title) VALUES (?, ?, now(), now(), ?, ?, ?)"
        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                val coupon = coupons[i]
                ps.setString(1, UUID.randomUUID().toString())
                ps.setLong(2, coupons[i].couponId)
                ps.setLong(3, coupons[i].userId)
                ps.setString(4, "${coupons[i].name} 쿠폰이 만료되었습니다.")
                ps.setString(5, "${coupons[i].name} 쿠폰이 만료되어 만료 쿠폰함으로 이동하였습니다.")
            }

            override fun getBatchSize(): Int {
                return coupons.size
            }
        })
    }
}