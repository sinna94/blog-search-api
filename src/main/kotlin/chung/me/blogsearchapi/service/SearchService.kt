package chung.me.blogsearchapi.service

import chung.me.blogsearchapi.controller.SearchParams
import chung.me.blogsearchapi.entity.SearchCount
import chung.me.blogsearchapi.searchclient.SearchResult
import chung.me.blogsearchapi.searchclient.factory.SearchClientFactory
import chung.me.blogsearchapi.searchclient.factory.SearchClientType
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class SearchService(
  private val searchClientFactory: SearchClientFactory,
  private val searchCountService: SearchCountService,
) {

  fun searchBlog(searchParams: SearchParams): SearchResult {
    val searchResult = search(searchParams, SearchClientType.KAKAO, SearchClientType.NAVER)
    try {
      searchCountService.increaseSearchCount(searchParams.query)
    } catch (e: DataIntegrityViolationException) {
      searchCountService.increaseSearchCount(searchParams.query)
    }
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
    } catch (e: ResponseStatusException) {
      if (e.statusCode.is4xxClientError) {
        throw e
      }
      if (backupClientType != null) {
        return search(searchParams, backupClientType)
      }
      throw e
    }
  }

  @Transactional(readOnly = true)
  fun searchTop10Queries(): List<SearchCountResponse> {
    return searchCountService.searchTop10Queries()
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
