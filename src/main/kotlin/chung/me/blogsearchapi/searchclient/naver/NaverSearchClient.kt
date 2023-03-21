package chung.me.blogsearchapi.searchclient.naver

import chung.me.blogsearchapi.searchclient.AbstractSearchClient
import chung.me.blogsearchapi.searchclient.SearchResult
import chung.me.blogsearchapi.searchclient.SortType
import chung.me.blogsearchapi.searchclient.factory.SearchClientType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class NaverSearchClient(
  @Value("\${api.naver.client-id}") private val clientId: String,
  @Value("\${api.naver.client-secret}") private val clientSecret: String,
  @Value("\${api.naver.base-url}") private val baseUrl: String,
  @Value("\${api.naver.path}") private val path: String,
) : AbstractSearchClient() {

  override fun search(query: String, sort: SortType?, page: Int?, size: Int?): SearchResult {
    logger.info("Searching Naver")

    return buildWebClient()
      .get()
      .uri { uriBuilder ->
        uriBuilder.path(path)
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
    .baseUrl(baseUrl)
    .defaultHeaders {
      it.set("X-Naver-Client-Id", clientId)
      it.set("X-Naver-Client-Secret", clientSecret)
    }
    .build()
}
