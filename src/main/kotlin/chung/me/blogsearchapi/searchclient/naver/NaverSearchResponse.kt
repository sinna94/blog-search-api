package chung.me.blogsearchapi.searchclient.kakao

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class KakaoSearchResponse(
  val meta: Meta,
  val documents: List<Document>,
)

data class Meta(
  val totalCount: Int,
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
  val dateTime: OffsetDateTime,
)
