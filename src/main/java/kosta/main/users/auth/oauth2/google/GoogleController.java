package kosta.main.users.auth.oauth2.google;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/authorization/google")
public class GoogleController {

    @GetMapping
    public ResponseEntity<?> getAuthorization(HttpServletRequest request){
        String header = request.getHeader("Authorization");

        return new ResponseEntity(header, HttpStatus.OK);
    }
}
