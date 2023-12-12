package seoultech.app.domain.notification

import java.util.*

//import jakarta.persistence.Entity
//import seoultech.app.domain.common.BaseEntity

//@Entity
class Notification (
        val id: UUID,
        val userId: Long,
        val couponId: Long,
        val title: String,
        val content: String
)
//    : BaseEntity()
{
}