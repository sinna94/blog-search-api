package chung.me.blogsearchapi.repository

import chung.me.blogsearchapi.entity.SearchCount
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface SearchCountRepository : JpaRepository<SearchCount, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  fun findByQuery(query: String): SearchCount?

  @Query("SELECT s FROM SearchCount s ORDER BY s.count DESC LIMIT 10")
  fun findTop10Queries(): MutableList<SearchCount>
}
