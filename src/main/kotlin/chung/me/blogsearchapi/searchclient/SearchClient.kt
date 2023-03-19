package chung.me.blogsearchapi.searchclient

interface SearchClient {
  fun search(query: String, sort: SortType? = null, page: Int? = null, size: Int? = null): SearchResult
}
