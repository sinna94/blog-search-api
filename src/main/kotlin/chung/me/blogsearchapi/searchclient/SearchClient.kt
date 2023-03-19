package chung.me.blogsearchapi.searchclient

interface SearchClient {
  fun search(query: String, sort: SortType?, page: Int?, size: Int?): SearchResult
}
