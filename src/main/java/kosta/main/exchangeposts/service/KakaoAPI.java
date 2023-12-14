package kosta.main.exchangeposts.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
public class KakaoAPI {

  @Value("${kakao_apikey}")
  private String kakao_apikey; // 카카오 API 키를 Spring의 @Value 어노테이션을 사용해 주입받음.

  public String getLocation() {
    try {
      // 맥도날드라는 단어를 UTF-8 형식으로 인코딩.
      String query = URLEncoder.encode("맥도날드", "UTF-8");

      // 카카오 API의 URL 구성. 검색 쿼리(query)와 반경(radius) 파라미터를 포함.
      String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.JSON?" +
          "query=" + query
          + "&size=" + 1
          + "&radius=" + "100";
      URL url = new URL(apiURL); // URL 객체 생성.

      HttpURLConnection con = (HttpURLConnection)url.openConnection(); // URL 연결을 위한 HttpURLConnection 객체 생성.

      // API 요청에 필요한 헤더 설정. 여기서는 인증 키를 'Authorization' 헤더에 추가.
      con.setRequestProperty("Authorization", "KakaoAK " + kakao_apikey);
      con.setRequestMethod("GET"); // HTTP GET 메소드로 요청.

      // 서버로부터 받은 응답 코드를 확인.
      int responseCode = con.getResponseCode();
      BufferedReader br;

      // HTTP 상태 코드가 200(정상)인 경우, 응답을 읽기 위해 InputStream 사용.
      if(responseCode == 200) {
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {  // 그렇지 않은 경우, 에러 스트림 사용.
        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }

      // 응답을 한 줄씩 읽어서 String 형태로 변환.
      String inputLine;
      StringBuffer response = new StringBuffer(); // 응답을 저장할 StringBuffer 객체.
      while ((inputLine = br.readLine()) != null) {
        response.append(inputLine); // 한 줄씩 읽어서 StringBuffer에 추가.
      }
      br.close(); // BufferedReader 닫기.

      return response.toString(); // 변환된 문자열을 반환.
    } catch (Exception e) {
      System.out.println(e); // 예외 발생 시 출력.
    }
    return ""; // 예외 발생 시 빈 문자열 반환.
  }
}
