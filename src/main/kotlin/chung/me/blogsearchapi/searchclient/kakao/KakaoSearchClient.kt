package chung.me.blogsearchapi.searchclient.kakao

import chung.me.blogsearchapi.searchclient.*
import chung.me.blogsearchapi.searchclient.factory.SearchClientType
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class KakaoSearchClient : AbstractSearchClient() {
  @Value("\${auth.kakao.key}")
  private lateinit var key: String

  override fun search(query: String, sort: SortType?, page: Int?, size: Int?): SearchResult {
    logger.info("Searching Kakao")

    return buildWebClient()
      .get()
      .uri { uriBuilder ->
        uriBuilder.path("/v2/search/blog")
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
    .baseUrl("https://dapi.kakao.com")
    .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK $key")
    .build()
}
