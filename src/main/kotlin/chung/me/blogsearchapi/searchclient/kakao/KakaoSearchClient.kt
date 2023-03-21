package chung.me.blogsearchapi.searchclient.kakao

import chung.me.blogsearchapi.searchclient.*
import chung.me.blogsearchapi.searchclient.factory.SearchClientType
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class KakaoSearchClient(
  @Value("\${api.kakao.key}") private val key: String,
  @Value("\${api.kakao.base-url}") private val baseUrl: String,
  @Value("\${api.kakao.path}") private val path: String,
) : AbstractSearchClient() {

  override fun search(query: String, sort: SortType?, page: Int?, size: Int?): SearchResult {
    logger.info("Searching Kakao")

    return buildWebClient()
      .get()
      .uri { uriBuilder ->
        uriBuilder.path(path)
          .queryParam("query", query)
        sort?.let { uriBuilder.queryParam("sort", it.toSpecificSortType(SearchClientType.KAKAO)) }
        page?.let { uriBuilder.queryParam("page", it) }
        size?.let { uriBuilder.queryParam("size", it) }
        uriBuilder.build()
      }.let {
        getSearchResponse<KakaoSearchResponse>(it)
      }
  }

  private fun buildWebClient() = WebClient.builder()
    .baseUrl(baseUrl)
    .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK $key")
    .build()
}
