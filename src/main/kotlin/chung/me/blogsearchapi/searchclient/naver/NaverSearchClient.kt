package chung.me.blogsearchapi.searchclient.naver

import chung.me.blogsearchapi.searchclient.AbstractSearchClient
import chung.me.blogsearchapi.searchclient.SearchResult
import chung.me.blogsearchapi.searchclient.SortType
import chung.me.blogsearchapi.searchclient.factory.SearchClientType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class NaverSearchClient : AbstractSearchClient() {

  @Value("\${auth.naver.client-id}")
  private lateinit var clientId: String

  @Value("\${auth.naver.client-secret}")
  private lateinit var clientSecret: String

  override fun search(query: String, sort: SortType?, page: Int?, size: Int?): SearchResult {
    logger.info("Searching Naver")

    return buildWebClient()
      .get()
      .uri { uriBuilder ->
        uriBuilder.path("/v1/search/blog")
          .queryParam("query", query)
        sort?.let { uriBuilder.queryParam("sort", it.toSpecificSortType(SearchClientType.NAVER)) }
        page?.let { uriBuilder.queryParam("start", it) }
        size?.let { uriBuilder.queryParam("display", it) }
        uriBuilder.build()
      }.let {
        getSearchResponse<NaverSearchResponse>(it)
      }
  }

  private fun buildWebClient() = WebClient.builder()
    .baseUrl("https://openapi.naver.com")
    .defaultHeaders {
      it.set("X-Naver-Client-Id", clientId)
      it.set("X-Naver-Client-Secret", clientSecret)
    }
    .build()
}
