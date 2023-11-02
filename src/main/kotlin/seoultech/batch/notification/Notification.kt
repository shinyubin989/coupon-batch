package seoultech.batch.notification

import jakarta.persistence.Entity
import seoultech.batch.common.BaseEntity

@Entity
class Notification (
        val userId: Long,
        val couponId: Long,
        val title: String,
        val content: String
) : BaseEntity() {
}