package seoultech.app.batch

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.dao.DataAccessResourceFailureException
import java.util.concurrent.CopyOnWriteArrayList


class NoOffsetItemReader<T> : JpaPagingItemReader<T>() {
    override fun doReadPage() {
        TODO("Not yet implemented")
    }

}