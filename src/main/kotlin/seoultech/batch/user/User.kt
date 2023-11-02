package seoultech.batch.user

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import seoultech.batch.common.BaseEntity

@Entity
class User(
    @Enumerated(EnumType.STRING)
    var userStatus: UserStatus = UserStatus.ACTIVE
) : BaseEntity() {

}