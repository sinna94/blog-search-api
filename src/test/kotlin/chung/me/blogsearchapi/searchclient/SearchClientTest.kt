package chung.me.blogsearchapi.searchclient

import chung.me.blogsearchapi.searchclient.kakao.KakaoSearchClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SearchClientTest {

  private lateinit var mockBackend: MockWebServer

  @BeforeEach
  fun setup() {
    mockBackend = MockWebServer()
    mockBackend.start()
  }

  @Test
  fun search() {
    val client = KakaoSearchClient("key", "http://localhost:${mockBackend.port}", "//v2/search/blog")
    mockBackend.enqueue(
      MockResponse().setBody(
        "{\"documents\":[{\"blogname\":\"세상을 바꾸는 새로운 기술\",\"contents\":\"Open AI에서 Chat <b>GPT</b> 4 버전을 출시하였습니다...\",\"datetime\":\"2023-03-15T09:27:43.000+09:00\",\"thumbnail\":\"https://search1.kakaocdn.net/argon/130x130_85_c/KN3Lc6COVwX\",\"title\":\"Chat <b>GPT</b> 4 출시\",\"url\":\"http://penguin5.tistory.com/150\"},{\"blogname\":\"달보기\",\"contents\":\"경험\\u0026#39; 및 \\u0026#39;콘텐츠 생성\\u0026#39; 기능을 제공하기 위해 완전히 새로운 인공지능(AI) 기반 Bing \\u0026#39;검색 엔진\\u0026#39; 및 \\u0026#39;에지(Edge) 브라우저\\u0026#39;를 출시했다고 합니다...\",\"datetime\":\"2023-03-19T22:34:11.000+09:00\",\"thumbnail\":\"https://search3.kakaocdn.net/argon/130x130_85_c/7WrNw2CgPdU\",\"title\":\"Bing 챗<b>GPT</b>분석 리뷰\",\"url\":\"http://moon.goodnyang.com/9\"}],\"meta\":{\"is_end\":false,\"pageable_count\":800,\"total_count\":145348}}"
      ).addHeader("Content-Type", "application/json")
        .setStatus("HTTP/1.1 200 OK")
    )

    val searchResult = client.search("gpt")
    val expected = SearchResult(
      145348, 800,
      listOf(
        PostInfo(
          "Chat <b>GPT</b> 4 출시",
          "Open AI에서 Chat <b>GPT</b> 4 버전을 출시하였습니다...",
          "http://penguin5.tistory.com/150",
          "세상을 바꾸는 새로운 기술",
          "https://search1.kakaocdn.net/argon/130x130_85_c/KN3Lc6COVwX",
          "2023-03-15T09:27:43.000+09:00"
        ),
        PostInfo(
          "Bing 챗\u003cb\u003eGPT\u003c/b\u003e분석 리뷰",
          "경험\u0026#39; 및 \u0026#39;콘텐츠 생성\u0026#39; 기능을 제공하기 위해 완전히 새로운 인공지능(AI) 기반 Bing \u0026#39;검색 엔진\u0026#39; 및 \u0026#39;에지(Edge) 브라우저\u0026#39;를 출시했다고 합니다...",
          "http://moon.goodnyang.com/9",
          "달보기",
          "https://search3.kakaocdn.net/argon/130x130_85_c/7WrNw2CgPdU",
          "2023-03-19T22:34:11.000+09:00",
        )
      )
    )

    println()

    assertEquals(expected, searchResult)
  }
}
