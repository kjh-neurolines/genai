package net.neurolines.genai.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.neurolines.genai.model.AuthUser;
import net.neurolines.genai.model.LoginRequest;
import net.neurolines.genai.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    @Qualifier("authApiRestTemplate")
    private RestTemplate authTemplate;

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request,  HttpServletResponse response) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //API 호출 시 파일 전송을 위해 설정
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> body = new HashMap<>();
        body.put("EMAIL", request.getEmail());
        body.put("password", request.getPassword());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);


        try {
            Map<String, Object> responseBody;
            ResponseEntity<Map<String, Object>> responseTemplate = authTemplate.exchange("/login", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

            responseBody = responseTemplate.getBody();

            if(responseTemplate.getStatusCode() == HttpStatus.OK) {

                // 쿠키 생성 및 설정
                Cookie cookie = new Cookie("jwt", responseBody.get("access_token").toString());
                cookie.setHttpOnly(true);      // JavaScript 접근 불가// HTTPS에서만 전송
                cookie.setPath("/");           // 모든 경로에 적용
                cookie.setMaxAge(Integer.parseInt(String.valueOf(responseBody.get("expires_in"))));     // 30분 토큰 만료

                Cookie mailCookie = new Cookie("email", request.getEmail());
                mailCookie.setHttpOnly(true);      // JavaScript 접근 불가// HTTPS에서만 전송
                mailCookie.setPath("/");           // 모든 경로에 적용
                mailCookie.setMaxAge(Integer.parseInt(String.valueOf(responseBody.get("expires_in"))));     // 30분 토큰 만료

                response.addCookie(cookie);
                response.addCookie(mailCookie);

                log.info(responseBody.toString());

                return ResponseEntity.ok(responseBody.get("access_token").toString());

            }else{
                return ResponseEntity.ok("FAIL");
            }

        } catch (Exception e)
        {
            log.error(e.toString());
        }

        return ResponseEntity.ok("");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response) {

        String jwt = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("jwt")) {
                    jwt = value;
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //API 호출 시 파일 전송을 위해 설정
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + jwt);

        Map<String, String> body = new HashMap<>();
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            Map<String, Object> responseBody;
            ResponseEntity<Map<String, Object>> responseTemplate = authTemplate.exchange("/logout", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

            responseBody = responseTemplate.getBody();

            Cookie cookie = new Cookie("jwt", "");
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);

            response.addCookie(cookie);

            Cookie mailCookie = new Cookie("email", "");
            mailCookie.setHttpOnly(true);
            mailCookie.setPath("/");
            mailCookie.setMaxAge(0);

            response.addCookie(mailCookie);


            log.info(responseBody.get("message").toString());

            return ResponseEntity.ok(responseBody.get("message").toString());

        } catch (Exception e)
        {
            log.error(e.toString());
        }

        return ResponseEntity.ok("");

    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request,
                                          HttpServletResponse response) {

        return ResponseEntity.ok(authService.refresh(request, response));


    }


    @PostMapping("/user")
    public ResponseEntity<Object> user(HttpServletRequest request,
                                          HttpServletResponse response) {
        return ResponseEntity.ok(authService.user(request, response));
    }

}
