package chung.me.blogsearchapi.searchclient

interface ApiSearchResponse {
  fun toSearchResult(): SearchResult
}
