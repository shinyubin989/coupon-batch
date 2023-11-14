package seoultech.app.batch

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CouponRowMapper : RowMapper<CouponProjection> {
    override fun mapRow(rs: ResultSet, rowNum: Int): CouponProjection {
        return CouponProjection(
                rs.getLong("user_id"),
                rs.getLong("id"),
                rs.getString("name"))
    }
}