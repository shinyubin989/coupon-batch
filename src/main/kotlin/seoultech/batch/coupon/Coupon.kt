package seoultech.batch.coupon

import jakarta.persistence.Entity
import seoultech.batch.common.BaseEntity
import java.time.LocalDate

@Entity
class Coupon(
    var expireDate: LocalDate,
    val name: String
) : BaseEntity() {
}