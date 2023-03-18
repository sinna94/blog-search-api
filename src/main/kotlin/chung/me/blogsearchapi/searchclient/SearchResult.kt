package chung.me.blogsearchapi.searchclient

import chung.me.blogsearchapi.searchclient.kakao.Document
import chung.me.blogsearchapi.searchclient.naver.Item
import java.time.OffsetDateTime

class SearchResponse(
  val totalCount: Int,
  val pageableCount: Int,
  val postList: List<PostInfo>,
)

class PostInfo private constructor(
  val title: String,
  val contents: String,
  val url: String,
  val blogName: String,
  val thumbnail: String?,
  val postDataTime: String,
) {
  companion object {
    fun create(document: Document): PostInfo {
      val (title, contents, url, blogName, thumbnail, dateTime) = document
      return PostInfo(title, contents, url, blogName, thumbnail, dateTime)
    }

    fun create(item: Item): PostInfo {
      val (title, link, description, bloggerName, _, postDate) = item
      return PostInfo(title, link, description, bloggerName, null, postDate)
    }
  }
}
