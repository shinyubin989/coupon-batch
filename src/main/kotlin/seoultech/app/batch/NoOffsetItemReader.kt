package seoultech.app.batch

//import jakarta.persistence.EntityManager
//import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.jdbc.core.JdbcTemplate
import java.util.concurrent.CopyOnWriteArrayList


class NoOffsetItemReader<T>(
        private val jdbcTemplate: JdbcTemplate
) : JpaPagingItemReader<T>() {

    var start = 1;
    override fun doReadPage() {
        TODO("Not yet implemented")
    }

}