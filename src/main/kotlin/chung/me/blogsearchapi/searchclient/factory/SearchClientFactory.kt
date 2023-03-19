package chung.me.blogsearchapi.searchclient.factory

import chung.me.blogsearchapi.searchclient.SearchClient
import chung.me.blogsearchapi.searchclient.kakao.KakaoSearchClient
import chung.me.blogsearchapi.searchclient.naver.NaverSearchClient
import org.springframework.stereotype.Component

@Component
class SearchClientFactory(
  private val kakaoSearchClient: KakaoSearchClient,
  private val naverSearchClient: NaverSearchClient,
) {

  fun create(searchClientType: SearchClientType): SearchClient {
    return when (searchClientType) {
      SearchClientType.KAKAO -> kakaoSearchClient
      SearchClientType.NAVER -> naverSearchClient
    }
  }
}
