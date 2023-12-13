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
  private String kakao_apikey;

  public String getLocation() {
    try {
      // 맛집 단어 UTF-8로 인코딩
      String query = URLEncoder.encode("맥도날드", "UTF-8");

      // 파라미터를 사용하여 요청 URL을 구성한다.
      String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.JSON?" +
          "query=" + query
          + "&radius=" + "100";
      URL url = new URL(apiURL);
      HttpURLConnection con = (HttpURLConnection)url.openConnection();

      // 요청 헤더를 setRequestProperty로 지정해준다. 헤더가 더 많을시 더 추가하면 됨.
      con.setRequestProperty("Authorization", "KakaoAK " + kakao_apikey);
      con.setRequestMethod("GET");

      // 응답 코드 확인
      int responseCode = con.getResponseCode();
      BufferedReader br;

      // 정상 응답이 200이므로(http 상태코드)
      if(responseCode == 200) { // 정상 호출
        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {  // 에러 발생
        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = br.readLine()) != null) {
        response.append(inputLine);
      }
      br.close();
//            System.out.println(response.toString());
      return response.toString();
    } catch (Exception e) {
      System.out.println(e);
    }
    return "";
  }
}