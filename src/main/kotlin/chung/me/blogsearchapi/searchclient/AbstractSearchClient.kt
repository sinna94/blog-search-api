package chung.me.blogsearchapi.searchclient

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.server.ResponseStatusException

abstract class AbstractSearchClient : SearchClient {
  protected val logger: Logger = LoggerFactory.getLogger(this.javaClass)

  protected inline fun <reified T : ApiSearchResponse> SearchClient.getSearchResponse(spec: WebClient.RequestHeadersSpec<*>): SearchResult {
    return spec.retrieve()
      .onStatus(HttpStatusCode::is5xxServerError, ClientResponse::createException)
      .bodyToMono(T::class.java)
      .onErrorResume {
        if (it is WebClientResponseException) {
          throw ResponseStatusException(it.statusCode, it.message, it)
        }
        throw it
      }
      .block()
      ?.let(ApiSearchResponse::toSearchResult) ?: SearchResult(0, 0, emptyList())
  }
}
