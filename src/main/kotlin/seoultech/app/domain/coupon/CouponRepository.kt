package seoultech.app.domain.coupon

//import jakarta.transaction.Transactional
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.data.jpa.repository.Modifying
//import org.springframework.data.jpa.repository.Query
//
//interface CouponRepository : JpaRepository<Coupon, Long> {
//
//    @Modifying
//    @Query("""
//        update Coupon c
//        set c.expired = true
//        where c.id = :id
//    """)
//    fun updateCouponExpired(id: Long)
//
//}