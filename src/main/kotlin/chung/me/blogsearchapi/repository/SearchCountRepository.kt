package chung.me.blogsearchapi.repository

import chung.me.blogsearchapi.entity.SearchCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SearchCountRepository : JpaRepository<SearchCount, Long> {
  fun findByQuery(query: String): SearchCount?

  @Query("SELECT s FROM SearchCount s ORDER BY s.count DESC LIMIT 10")
  fun findTop10Queries(): MutableList<SearchCount>
}
