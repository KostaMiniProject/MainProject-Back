package kosta.main.users.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kosta.main.global.error.dto.ErrorBaseResponse;
import kosta.main.global.error.exception.AuthErrorCode;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException e){
            //토큰의 유효기간 만료
            setErrorResponse(response, AuthErrorCode.EXPIRED_TOKEN);
        }catch (JwtException | IllegalArgumentException e){
            //유효하지 않은 토큰
            setErrorResponse(response, AuthErrorCode.INVALID_TOKEN);
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            AuthErrorCode errorCode
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(errorCode.getCode(), errorCode.getMessage());
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorBaseResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
