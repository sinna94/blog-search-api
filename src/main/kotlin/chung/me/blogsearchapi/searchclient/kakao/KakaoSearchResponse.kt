package chung.me.blogsearchapi.searchclient.kakao

import chung.me.blogsearchapi.searchclient.ApiSearchResponse
import chung.me.blogsearchapi.searchclient.PostInfo
import chung.me.blogsearchapi.searchclient.SearchResult
import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoSearchResponse(
  val meta: Meta,
  val documents: List<Document>,
) : ApiSearchResponse {
  override fun toSearchResult(): SearchResult {
    val (totalCount, pageableCount, _) = meta
    val postInfoList = documents.map { document -> PostInfo.create(document) }
    return SearchResult(totalCount, pageableCount, postInfoList)
  }
}

data class Meta(
  @get:JsonProperty("total_count")
  val totalCount: Int,
  @get:JsonProperty("pageable_count")
  val pageableCount: Int,
  val isEnd: Boolean,
)

data class Document(
  val title: String,
  val contents: String,
  val url: String,
  @get:JsonProperty("blogname")
  val blogName: String,
  val thumbnail: String,
  @get:JsonProperty("datetime")
  val dateTime: String,
)
