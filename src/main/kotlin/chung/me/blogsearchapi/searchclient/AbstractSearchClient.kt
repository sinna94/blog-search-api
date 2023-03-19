package chung.me.blogsearchapi.searchclient

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

abstract class AbstractSearchClient : SearchClient {
  protected val logger: Logger = LoggerFactory.getLogger(this.javaClass)

  protected inline fun <reified T : ApiSearchResponse> SearchClient.getSearchResponse(spec: WebClient.RequestHeadersSpec<*>): SearchResult {
    return spec.retrieve()
      .onStatus({ status -> status.isError }) { response ->
        if (response.statusCode().is4xxClientError) {
          response.createError<RuntimeException>()
        } else {
          response.createException()
        }
      }
      .bodyToMono(T::class.java)
      .onErrorResume {
        throw it
      }
      .retry(3)
      .delayElement(Duration.ofMillis(100))
      .block()
      ?.let(ApiSearchResponse::toSearchResult) ?: SearchResult(0, 0, emptyList())
  }
}
