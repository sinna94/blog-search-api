package chung.me.blogsearchapi.controller

import chung.me.blogsearchapi.searchclient.SearchResult
import chung.me.blogsearchapi.searchclient.SortType
import chung.me.blogsearchapi.service.SearchCountResponse
import chung.me.blogsearchapi.service.SearchService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
  private val searchService: SearchService,
) {

  @GetMapping
  fun searchBlog(
    searchParams: SearchParams,
//    @RequestParam query: String,
//    @RequestParam(required = false) sort: SortType?,
//    @RequestParam(required = false) page: Int?,
//    @RequestParam(required = false) size: Int?,
  ): ResponseEntity<SearchResult> {
    return ResponseEntity.ok(searchService.searchBlog(searchParams))
  }

  @GetMapping("/popular")
  fun searchPopularKeyword(): ResponseEntity<List<SearchCountResponse>> {
    return ResponseEntity.ok(searchService.searchTop10Queries())
  }
}

data class SearchParams(
  val query: String,
  val sort: SortType?,
  val page: Int?,
  val size: Int?,
)
