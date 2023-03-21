# blog-search-api

## 실행 환경
Java 17

## 실행 방법
```bash
java -jar ./blog-search-api.jar --KAKAO_KEY={KAKAO_API_KEY} --NAVER_CLIENT_ID={NAVER_API_CLIENT_ID} --NAVER_CLIENT_SECRET={NAVER_API_CLIENT_SECRET}
```

## Executable jar 다운로드 링크
[Executable jar 다운로드 링크](https://github.com/sinna94/blog-search-api/raw/master/blog-search-api.jar)

## API 명세

### 블로그 검색

```http request
GET http://localhost:8080/api/v1/search?query={{$query}}&sort={{$sort}}&page={{$page}}&size={{$size}}
```

#### 파라미터

| 파라미터  | 설명                                       | nullable |
|-------|------------------------------------------|-------|
| query | 검색어                                      | X     |
| sort  | 정렬 조건, accuracy(정확도순), recency(최신순) 중 선택 | O     |
| page  | 결과 페이지 번호                                | O     |
| size  | 한 페이지에 보여질 문서 수                          | O     |

#### 응답

| 이름            | 설명                      |
|---------------|-------------------------|
| totalCount    | 검색된 문서 수                |
| pageableCount | totalCount 중 노출 가능 문서 수 |
| postList      | 문서 정보                   |

```json
{
  "totalCount": 17290610,
  "pageableCount": 798,
  "postList": [
    {
      "title": "[Flutter] Flutter <b>Kakao</b> Api 사용하기 (1) - sdk 설치",
      "contents": "flutter <b>kakao</b> api에는 아래의 기능들을 포함하여 여러가지 기능들을 sdk 형식으로 제공하여, 원하는 api를 sdk 설치를 통해 사용하면 된다. <b>카카오</b> 로그인 <b>카카오</b>톡 소셜 <b>카카오</b>스토리 <b>카카오</b>톡 공유 <b>카카오</b> 내비 설치 1. pubspec.yaml 파일에 의존성 설정 pubspec.yaml 파일에 사용할 sdk를 추가한 후 저장을 누르면...",
      "url": "http://lims-dev.tistory.com/38",
      "blogName": "개발은 개 발로",
      "thumbnail": "https://search3.kakaocdn.net/argon/130x130_85_c/7EifFoYndUK",
      "postDataTime": "2023-03-21T12:59:38.000+09:00"
    },
    ...
  ]
}
```

### 인기 검색어 목록 조회

```http request
GET http://localhost:8080/api/v1/search/popular
```

#### 응답

| 이름    | 설명     |
|-------|--------|
| query | 검색어    |
| count | 검색된 횟수 |

```json
[
  {
    "query": "kakao1",
    "count": 4
  },
  {
    "query": "kakao",
    "count": 1
  },
  {
    "query": "kakao13",
    "count": 1
  }
]
```

## 사용한 오픈소스

| 오픈소스                                 | 목적                    |
|--------------------------------------|-----------------------|
| `org.jlleitschuh.gradle.ktlint`      | 코드 린팅                 |
| `org.apache.commons:commons-lang3`   | Exception 가공 처리       |
| `com.squareup.okhttp3:okhttp`        | WebClient Mocking 테스트 |
| `com.squareup.okhttp3:mockwebserver` | WebClient Mocking 테스트 |
