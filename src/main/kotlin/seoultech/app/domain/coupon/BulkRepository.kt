package seoultech.app.domain.coupon

import jakarta.transaction.Transactional
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import seoultech.app.batch.CouponProjection
import java.sql.PreparedStatement
import javax.sql.DataSource

@Repository
class BulkRepository(
        private val jdbcTemplate: JdbcTemplate,
        private val dataSource: DataSource
) {

    @Transactional
    fun saveAll(coupons: List<CouponProjection>) {
        val sql =
                """
                    INSERT INTO notification (coupon_id, created_at, updated_at, user_id, content, title)
                    VALUES (?, now(), now(), ?, ?, ?)
                    """
        jdbcTemplate.batchUpdate(sql,
                object: BatchPreparedStatementSetter {
                    override fun setValues(ps: PreparedStatement, i: Int) {
                        ps.setLong(1, coupons[i].couponId)
                        ps.setLong(2, coupons[i].userId)
                        ps.setString(3, "${coupons[i].name} 쿠폰이 만료되었습니다.")
                        ps.setString(4, "${coupons[i].name} 쿠폰이 만료되어 만료 쿠폰함으로 이동하였습니다.")
                    }

                    override fun getBatchSize() = coupons.size
                }
        )
    }

    @Transactional
    fun updateAll(coupons: List<CouponProjection>) {
        val connection = dataSource.connection
        val statement = connection.prepareStatement(
                """
                    UPDATE coupon
                    SET expired = true
                    where id = ?
                    """.trimIndent()
        )!!

        try {
            for (coupon in coupons) {
                statement.setLong(1, coupon.couponId)
                statement.addBatch()
            }
            statement.executeBatch()
        } catch (e: Exception) {
            throw e
        } finally {
            if (statement.isClosed.not()) {
                statement.close()
            }
            if (connection.isClosed.not()) {
                connection.close()
            }
        }
    }

    @Transactional
    fun updateAll2(coupons: List<CouponProjection>) {
        val sql =
                """
                    UPDATE coupon
                    SET expired = true
                    where id = ?
                    """.trimIndent()
        jdbcTemplate.batchUpdate(sql,
                object: BatchPreparedStatementSetter {
                    override fun setValues(ps: PreparedStatement, i: Int) {
                        ps.setLong(1, coupons[i].couponId)
                    }
                    override fun getBatchSize() = coupons.size
                }
        )
    }
}