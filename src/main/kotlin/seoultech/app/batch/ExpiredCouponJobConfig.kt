package seoultech.app.batch

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.batch.item.support.CompositeItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import seoultech.app.batch.listener.*
import seoultech.app.domain.coupon.BulkRepository
import seoultech.app.domain.coupon.CouponRepository
import seoultech.app.domain.notification.Notification
import seoultech.app.domain.notification.NotificationRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.sql.DataSource


@Configuration
class ExpiredCouponJobConfig(
        private val jobRepository: JobRepository,
        private val transactionManager: PlatformTransactionManager,
        private val entityManagerFactory: EntityManagerFactory,
        private val dataSource: DataSource,
        private val couponJobExecutionListener: CouponJobExecutionListener,
        private val couponStepExecutionListener: CouponStepExecutionListener,
//        private val couponChunkListener: CouponChunkListener,
//        private val couponItemReadListener: CouponItemReadListener,
//        private val couponItemWriterListener: CouponItemWriterListener,
        private val couponRepository: CouponRepository,
        private val notifiCationRepository: NotificationRepository,
        private val bulkRepository: BulkRepository,
//        private val couponsRepository: CouponsRepository
) {

    private val chunkSize = 10000
    private val yesterday = LocalDate.now().minusDays(1L)
    private val logger = KotlinLogging.logger {}
    private var selectStandardId = 0
    private var selectStartTime = LocalDateTime.now()
    private var updateStartTime = LocalDateTime.now()
    private var insertStartTime = LocalDateTime.now()
    private var insertEndTime = LocalDateTime.now()

    @Bean
    fun notificationJob(step: Step): Job {
        return JobBuilder("notificationJob1", jobRepository)
                .incrementer(RunIdIncrementer())
                .start(step)
                .listener(couponJobExecutionListener)
                .build()
    }

    @Bean
    @JobScope
    fun step(): Step {

        return StepBuilder("step", jobRepository)
                .listener(couponStepExecutionListener)
                .chunk<CouponProjection, CouponProjection>(chunkSize, transactionManager)
                .reader(jdbcCursorReader())
//                .writer(updateWithJpa())
                .writer(updateWithBatchStatement())
//                .reader(reader())
//                .reader(noIndexReader())
//                .writer(testWriter())
//                .writer(insertExpiredNotification())
//                .writer(batchUpdate())
//                .writer(batchUpdate2())
//                .writer(batchInsert())
//                .writer(compositeWriter())

//                .writer(updateExpiredCoupon())
//                .listener(couponItemReadListener)
//                .listener(couponItemWriterListener)
//                .listener(couponChunkListener)
//                .allowStartIfComplete(true)

                .build()
    }

    @Bean
    @StepScope
    fun jdbcCursorReader(): JdbcCursorItemReader<CouponProjection> {
        selectStartTime = LocalDateTime.now()
        return JdbcCursorItemReaderBuilder<CouponProjection>()
                .fetchSize(chunkSize)
                .sql("""
                    select user_id, id, name
                    from coupon as c
                    where expire_date <= ? and used = false
                    """.trimIndent())
                .queryArguments(yesterday)
                .rowMapper(CouponRowMapper())
                .name("jdbcCursorReader")
                .dataSource(dataSource)
                .build()
    }
//
//    @Bean
//    fun updateWithExposed(coupons: List<CouponProjection>) {
//        transaction {
//            for (coupon in coupons) {
//                Coupon.update({ Writers.id eq id })
//                {
//                    it[email] = "update"
//                }
//            }
//        }
//    }

    @Bean
    fun updateWithJpa(): ItemWriter<CouponProjection> {
        return ItemWriter { coupons ->
            for (coupon in coupons) {
                couponRepository.updateCouponExpired(coupon.couponId)
            }
        }
    }

    @Bean
    @StepScope
    fun updateWithBatchStatement(): ItemWriter<CouponProjection> = ItemWriter { coupons ->
        val sql = "update coupon c set c.expired = true where c.id = ?".trimIndent()
        val connection = dataSource.connection
        val statement = connection.prepareStatement(sql)!!
        try {
            for (coupon in coupons) {
//                logger.info { coupon.couponId }
                statement.apply {
                    this.setLong(1, coupon.couponId)
                }
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

    @Bean
    @StepScope
    fun reader(): JpaPagingItemReader<CouponProjection> {
        selectStartTime = LocalDateTime.now()
        val itemReader = JpaPagingItemReaderBuilder<CouponProjection>()
                .pageSize(chunkSize)
                .entityManagerFactory(entityManagerFactory)
                .queryString("""
                    select new seoultech.app.batch.CouponProjection(c.userId as userId, c.id as couponId, c.name as name)
                    from Coupon as c
                    where c.expireDate <= :yesterday and c.used = false
                    """.trimIndent())
                .parameterValues(mapOf<String?, Any>("yesterday" to yesterday))
                .name("JpaPagingItemReader")
                .build()
        return itemReader
    }

    @Bean
    fun testWriter(): ItemWriter<CouponProjection> {
        return ItemWriter { coupons ->
            for (coupon in coupons) {
                if(coupon.couponId % 10000L == 0L) logger.info { coupon.name }
//                couponRepository.updateCouponExpired(coupon.couponId)
            }
        }
    }

    @Bean
    @StepScope
    fun compositeWriter(): CompositeItemWriter<CouponProjection> {
        val compositeItemWriter = CompositeItemWriter<CouponProjection>()
        compositeItemWriter.setDelegates(listOf(updateExpiredCoupon(), insertExpiredNotification()))
        return compositeItemWriter
    }

    @Bean
    fun updateExpiredCoupon(): ItemWriter<CouponProjection> {
        return ItemWriter { coupons ->
            for (coupon in coupons) {
                if(coupon.couponId % 10000 == 0L) logger.info { coupon.name}
                couponRepository.updateCouponExpired(coupon.couponId)
            }
        }

    }

    @Bean
    fun batchUpdate(): ItemWriter<CouponProjection> {
        return ItemWriter { coupons ->
            logger.info { coupons.items[0].name }
            bulkRepository.updateAll(coupons.items)
        }
    }

    @Bean
    fun batchUpdate2(): ItemWriter<CouponProjection> {
        return ItemWriter { coupons ->
            logger.info { coupons.items[0].name }
            bulkRepository.updateAll2(coupons.items)
        }
    }

//    @Bean
//    fun exposedBatchUpdate(): ItemWriter<CouponProjection> = ItemWriter { payments ->
//        transaction(
//                exposedDataBase
//        ) {
//            PaymentBack.batchInsert(
//                    data = payments,
//                    shouldReturnGeneratedValues = false
//            ) { payment ->
//                this[PaymentBack.orderId] = payment.orderId
//                this[PaymentBack.amount] = payment.amount
//            }
//        }
//    }


    @Bean
    fun batchInsert(): ItemWriter<CouponProjection> {
        return ItemWriter { coupons ->
            logger.info { coupons.items[0].name }
            bulkRepository.saveAll(coupons.items)
        }
    }

    @Bean
    fun insertExpiredNotification(): ItemWriter<CouponProjection> {
        return ItemWriter { coupons ->
            for (coupon in coupons) {
                if(coupon.couponId % 10000 == 0L) logger.info { coupon.name }
                notifiCationRepository.save(Notification(coupon.userId, coupon.couponId, coupon.name + " 쿠폰이 만료되었습니다.", coupon.name + " 쿠폰이 만료되어 만료 쿠폰함으로 이동하였습니다."))
            }
        }
    }
}