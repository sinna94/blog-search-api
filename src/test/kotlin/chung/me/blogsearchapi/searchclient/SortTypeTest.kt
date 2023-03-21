package chung.me.blogsearchapi.searchclient

import chung.me.blogsearchapi.searchclient.factory.SearchClientType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SortTypeTest {

  companion object {
    @JvmStatic
    fun specificSortTypeArgs() = Stream.of(
      Arguments.of(
        SortType.ACCURACY, SearchClientType.KAKAO, "accuracy"
      ),
      Arguments.of(
        SortType.ACCURACY, SearchClientType.NAVER, "sim"
      ),
      Arguments.of(
        SortType.RECENCY, SearchClientType.KAKAO, "recency"
      ),
      Arguments.of(
        SortType.RECENCY, SearchClientType.NAVER, "date"
      )
    )

    @JvmStatic
    fun ofArgs() = Stream.of(
      Arguments.of("accuracy", SortType.ACCURACY),
      Arguments.of("recency", SortType.RECENCY)
    )
  }

  @MethodSource("specificSortTypeArgs")
  @ParameterizedTest
  @DisplayName("검색 타입별 검색 조건 변환 테스트")
  fun toSpecificSortTypeTest(sortType: SortType, searchClientType: SearchClientType, expected: String) {
    assertEquals(expected, sortType.toSpecificSortType(searchClientType))
  }

  @MethodSource("ofArgs")
  @ParameterizedTest
  @DisplayName("source 를 SortType 으로 변환 테스트")
  fun ofTest(source: String, expected: SortType) {
    assertEquals(expected, SortType.of(source))
  }

  @Test
  @DisplayName("잘못된 source 로 SortType 변환 테스트")
  fun ofExceptionTest() {
    assertThrows(IllegalArgumentException::class.java, {
      SortType.of("wrongSource")
    }, "SortType must be one of accuracy, recency")
  }
}
