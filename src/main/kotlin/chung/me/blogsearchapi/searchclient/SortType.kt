package chung.me.blogsearchapi.searchclient

import chung.me.blogsearchapi.searchclient.factory.SearchClientType
import com.fasterxml.jackson.annotation.JsonProperty

enum class SortType {
  @JsonProperty("accuracy")
  ACCURACY {
    override fun toSpecificSortType(searchClientType: SearchClientType): String {
      return when (searchClientType) {
        SearchClientType.KAKAO -> "accuracy"
        SearchClientType.NAVER -> "sim"
      }
    }
  },
  @JsonProperty("recency")
  RECENCY {
    override fun toSpecificSortType(searchClientType: SearchClientType): String {
      return when (searchClientType) {
        SearchClientType.KAKAO -> "recency"
        SearchClientType.NAVER -> "date"
      }
    }
  };

  abstract fun toSpecificSortType(searchClientType: SearchClientType): String

  companion object {
    fun of(source: String): SortType {
      return when (source) {
        "accuracy" -> ACCURACY
        "recency" -> RECENCY
        else -> throw IllegalArgumentException("SortType must be one of accuracy, recency")
      }
    }
  }
}
