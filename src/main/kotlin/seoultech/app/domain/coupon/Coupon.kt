package seoultech.app.domain.coupon

import jakarta.persistence.Entity
import seoultech.app.domain.common.BaseEntity
import java.time.LocalDate

@Entity
class Coupon(
        val userId: Long,
        var expireDate: LocalDate,
        var used: Boolean,
        var expired: Boolean,
        val name: String
) : BaseEntity() {

}