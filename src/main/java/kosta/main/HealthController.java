package kosta.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    //testa

    @GetMapping
    public String healthCheck(){
        return "health ok!!!";
    }
}