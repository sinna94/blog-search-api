package chung.me.blogsearchapi.searchclient.naver

import chung.me.blogsearchapi.searchclient.ApiSearchResponse
import chung.me.blogsearchapi.searchclient.PostInfo
import chung.me.blogsearchapi.searchclient.SearchResult
import com.fasterxml.jackson.annotation.JsonProperty

data class NaverSearchResponse(
  val lastBuildDate: String,
  val total: Int,
  val start: Int,
  val display: Int,
  val items: List<Item>,
) : ApiSearchResponse {
  override fun toSearchResult(): SearchResult {
    val postInfoList = items.map { item -> PostInfo.create(item) }
    return SearchResult(total, start, postInfoList)
  }
}

data class Item(
  val title: String,
  val link: String,
  val description: String,
  @get:JsonProperty("bloggername")
  val bloggerName: String,
  @get:JsonProperty("bloggerlink")
  val bloggerLink: String,
  @get:JsonProperty("postdate")
  val postDate: String,
)
