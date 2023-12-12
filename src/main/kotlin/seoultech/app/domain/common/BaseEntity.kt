package seoultech.app.domain.common

//import jakarta.persistence.*
//import org.hibernate.annotations.CreationTimestamp
//import org.hibernate.annotations.UpdateTimestamp
//import org.springframework.data.jpa.domain.support.AuditingEntityListener
//import java.time.LocalDateTime
//
//@EntityListeners(AuditingEntityListener::class)
//@MappedSuperclass
//abstract class BaseEntity {
//
//    @Id
//    @GeneratedValue
//    var id: Long? = null
//        internal set
//
//    @CreatedDate
//    @Column(name = "created_at", nullable = false, updatable = false)
//    lateinit var createdAt: LocalDateTime
//        internal set
//
//    @UpdateTimestamp
//    @Column(name = "updated_at", nullable = false)
//    lateinit var updatedAt: LocalDateTime
//        internal set
//}