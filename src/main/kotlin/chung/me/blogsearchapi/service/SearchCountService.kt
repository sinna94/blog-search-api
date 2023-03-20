package chung.me.blogsearchapi.service

import chung.me.blogsearchapi.entity.SearchCount
import chung.me.blogsearchapi.repository.SearchCountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SearchCountService(
  private val searchCountRepository: SearchCountRepository,
) {

  @Transactional
  fun increaseSearchCount(query: String) {
    println("query: $query")
    val lowercaseQuery = query.lowercase()
    val searchCount = searchCountRepository.findByQuery(lowercaseQuery)
      ?: SearchCount(lowercaseQuery)
    searchCount.increaseCount()
    searchCountRepository.save(searchCount)
  }

  @Transactional(readOnly = true)
  fun searchTop10Queries(): List<SearchCountResponse> {
    return searchCountRepository.findTop10Queries().map { SearchCountResponse.create(it) }
  }
}
