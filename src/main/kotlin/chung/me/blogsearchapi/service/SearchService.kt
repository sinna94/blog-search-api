package chung.me.blogsearchapi.service

import chung.me.blogsearchapi.controller.SearchParams
import chung.me.blogsearchapi.entity.SearchCount
import chung.me.blogsearchapi.repository.SearchCountRepository
import chung.me.blogsearchapi.searchclient.SearchResult
import chung.me.blogsearchapi.searchclient.factory.SearchClientFactory
import chung.me.blogsearchapi.searchclient.factory.SearchClientType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SearchService(
  private val searchClientFactory: SearchClientFactory,
  private val searchCountRepository: SearchCountRepository,
) {

  @Transactional
  fun searchBlog(searchParams: SearchParams): SearchResult {
    val searchResult = search(searchParams, SearchClientType.KAKAO, SearchClientType.NAVER)
    increaseSearchCount(searchParams.query)
    return searchResult
  }

  private fun search(
    searchParams: SearchParams,
    clientType: SearchClientType,
    backupClientType: SearchClientType? = null,
  ): SearchResult {
    try {
      val (query, sort, page, size) = searchParams
      return searchClientFactory.create(clientType).search(query, sort, page, size)
    } catch (e: RuntimeException) {
      if (backupClientType != null) {
        return search(searchParams, backupClientType)
      }
      throw RuntimeException()
    }
  }

  private fun increaseSearchCount(query: String) {
    val searchCount = searchCountRepository.findByQuery(query) ?: SearchCount(query)
    searchCount.increaseCount()
    searchCountRepository.save(searchCount)
  }

  @Transactional(readOnly = true)
  fun searchTop10Queries(): List<SearchCountResponse> {
    return searchCountRepository.findTop10Queries().map { SearchCountResponse.create(it) }
  }
}

data class SearchCountResponse constructor(
  val query: String,
  val count: Long,
) {
  companion object {
    fun create(entity: SearchCount): SearchCountResponse {
      return SearchCountResponse(entity.query, entity.count)
    }
  }
}
