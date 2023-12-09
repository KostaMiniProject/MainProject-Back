package kosta.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {
    //testa
    @GetMapping()
    public String healthCheck(){
        return "health ok!!!";
    }

    private final Integer serverNumber;

    private HealthController(@Value("${server-number}") final int serverNumber) {
        this.serverNumber = serverNumber;
    }

    private int count = 0;

    //로드밸런서 확인용 api
    @GetMapping("/loadcheck")
    public ResponseEntity<Map<String, Integer>> count() {
        count ++;
        Map<String, Integer> data = new HashMap<>();
        data.put("Server Number", serverNumber);
        data.put("Visiting Count", count);
        return ResponseEntity.ok(data);
    }

    //무중단 배포용 api
    @GetMapping("/api/check")
    public String checkServerStatus(){
        return "check";
    }
}