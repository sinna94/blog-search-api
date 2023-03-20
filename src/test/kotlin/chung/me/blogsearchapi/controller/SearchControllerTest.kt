package chung.me.blogsearchapi.controller

import chung.me.blogsearchapi.MvcMockTestSupport
import chung.me.blogsearchapi.repository.SearchCountRepository
import chung.me.blogsearchapi.searchclient.PostInfo
import chung.me.blogsearchapi.searchclient.SearchResult
import chung.me.blogsearchapi.searchclient.kakao.KakaoSearchClient
import chung.me.blogsearchapi.searchclient.naver.NaverSearchClient
import chung.me.blogsearchapi.service.SearchCountResponse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class SearchControllerTest(
  private val searchCountRepository: SearchCountRepository,
) : MvcMockTestSupport() {

  @MockBean
  private lateinit var kakaoSearchClient: KakaoSearchClient

  @MockBean
  private lateinit var naverSearchClient: NaverSearchClient

  @BeforeEach
  fun setUp() {
    searchCountRepository.deleteAllInBatch()
  }

  @Test
  @DisplayName("카카오 블로그 검색 테스트")
  fun searchBlogUsingKakaoApi() {
    val now = LocalDateTime.now().toString()
    doReturn(
      SearchResult(
        100, 0,
        listOf(
          PostInfo("post1", "post1 contents", "url", "blog1", "thumbnail", now),
          PostInfo("post2", "post2 contents", "url", "blog1", "thumbnail", now),
          PostInfo("post3", "post3 contents", "url", "blog2", "thumbnail", now),
          PostInfo("post4", "post4 contents", "url", "blog3", "thumbnail", now),
          PostInfo("post5", "post5 contents", "url", "blog3", "thumbnail", now),
        )
      )
    ).`when`(
      kakaoSearchClient
    ).search("kakao")

    val response = performGet("/api/v1/search", mapOf("query" to "kakao")).andReturn().response
    assertEquals(HttpStatus.OK.value(), response.status)
    val searchResult = toResult<SearchResult>(response)

    val expected = SearchResult(
      100, 0,
      listOf(
        PostInfo("post1", "post1 contents", "url", "blog1", "thumbnail", now),
        PostInfo("post2", "post2 contents", "url", "blog1", "thumbnail", now),
        PostInfo("post3", "post3 contents", "url", "blog2", "thumbnail", now),
        PostInfo("post4", "post4 contents", "url", "blog3", "thumbnail", now),
        PostInfo("post5", "post5 contents", "url", "blog3", "thumbnail", now),
      )
    )

    assertEquals(expected, searchResult)
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "400",
      "401",
      "404"
    ]
  )
  @DisplayName("카카오 블로그 검색 4xx 리턴 테스트")
  fun return4xxUsingKakaoApi(statusCodeValue: Int) {
    doThrow(ResponseStatusException(HttpStatusCode.valueOf(statusCodeValue))).`when`(kakaoSearchClient).search("kakao")

    val response = performGet("/api/v1/search", mapOf("query" to "kakao")).andReturn().response
    assertEquals(statusCodeValue, response.status)
  }

  @Test
  @DisplayName("네이버 블로그 검색 테스트")
  fun searchBlogUsingNaverApi() {
    doThrow(ResponseStatusException(HttpStatusCode.valueOf(500))).`when`(kakaoSearchClient).search("kakao")

    val now = LocalDateTime.now().toString()
    doReturn(
      SearchResult(
        100, 0,
        listOf(
          PostInfo("post1", "post1 contents", "url", "blog1", "thumbnail", now),
          PostInfo("post2", "post2 contents", "url", "blog1", "thumbnail", now),
          PostInfo("post3", "post3 contents", "url", "blog2", "thumbnail", now),
          PostInfo("post4", "post4 contents", "url", "blog3", "thumbnail", now),
          PostInfo("post5", "post5 contents", "url", "blog3", "thumbnail", now),
        )
      )
    ).`when`(
      naverSearchClient
    ).search("kakao")

    val response = performGet("/api/v1/search", mapOf("query" to "kakao")).andReturn().response
    assertEquals(HttpStatus.OK.value(), response.status)
    val searchResult = toResult<SearchResult>(response)

    val expected = SearchResult(
      100, 0,
      listOf(
        PostInfo("post1", "post1 contents", "url", "blog1", "thumbnail", now),
        PostInfo("post2", "post2 contents", "url", "blog1", "thumbnail", now),
        PostInfo("post3", "post3 contents", "url", "blog2", "thumbnail", now),
        PostInfo("post4", "post4 contents", "url", "blog3", "thumbnail", now),
        PostInfo("post5", "post5 contents", "url", "blog3", "thumbnail", now),
      )
    )

    assertEquals(expected, searchResult)
  }

  @ParameterizedTest
  @CsvSource(
    value = [
      "400",
      "401",
      "404"
    ]
  )
  @DisplayName("네이버 블로그 검색 4xx 리턴 테스트")
  fun return4xxUsingNaverApi(statusCodeValue: Int) {
    doThrow(ResponseStatusException(HttpStatusCode.valueOf(500))).`when`(kakaoSearchClient).search("kakao")
    doThrow(ResponseStatusException(HttpStatusCode.valueOf(statusCodeValue))).`when`(kakaoSearchClient).search("kakao")

    val response = performGet("/api/v1/search", mapOf("query" to "kakao")).andReturn().response
    assertEquals(statusCodeValue, response.status)
  }

  @Test
  @DisplayName("멀티 스레드 환경에서 검색된 검색어의 수를 조회")
  fun searchPopularKeyword() {
    doReturn(
      SearchResult(
        0, 0, emptyList()
      )
    ).`when`(
      kakaoSearchClient
    ).search("query")

    listOf(
      Pair("kakao", 10),
      Pair("gpt", 4),
      Pair("google", 15),
      Pair("bing", 15),
      Pair("java", 5),
      Pair("java", 5),
      Pair("kotlin", 10),
      Pair("kotlin", 10),
      Pair("gradle", 5),
      Pair("lion", 13),
      Pair("google", 15),
      Pair("chat", 25),
      Pair("KAKAO", 6),
      Pair("apple", 3),
      Pair("mac", 9),
      Pair("mouse", 1),
    ).parallelStream()
      .forEach {
        val (query, count) = it
        (0 until count).forEach { _ ->
          callSearchForIncreaseSearchCount(query)
        }
      }

    val response = performGet("/api/v1/search/popular").andReturn().response
    assertEquals(HttpStatus.OK.value(), response.status)
    val countResponse = toResult<List<SearchCountResponse>>(response)
    assertThat(countResponse)
      .extracting("query", "count")
      .containsExactlyElementsOf(
        listOf(
          Tuple("google", 30L),
          Tuple("chat", 25L),
          Tuple("kotlin", 20L),
          Tuple("kakao", 16L),
          Tuple("bing", 15L),
          Tuple("lion", 13L),
          Tuple("java", 10L),
          Tuple("mac", 9L),
          Tuple("gradle", 5L),
          Tuple("gpt", 4L),
        )
      )
      .hasSize(10)
  }

  private fun callSearchForIncreaseSearchCount(query: String) {
    performGet("/api/v1/search", mapOf("query" to query)).andReturn().response
  }
}
