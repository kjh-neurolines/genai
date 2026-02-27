package net.neurolines.genai.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.neurolines.genai.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class AuthService {

    @Autowired
    @Qualifier("authApiRestTemplate")
    private RestTemplate authTemplate;

    public String refresh(HttpServletRequest request,
                          HttpServletResponse response)
    {
        String jwt = "";
        String email = "";

        Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기

        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if ("jwt".equals(name)) {
                    jwt = value;
                }else if("email".equals(name)){
                    email = value;
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //API 호출 시 파일 전송을 위해 설정
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + jwt);;

        Map<String, String> body = new HashMap<>();

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {

            Map<String, Object> responseBody;
            ResponseEntity<Map<String, Object>> responseTemplate = authTemplate.exchange("/refresh", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

            if(responseTemplate.getStatusCode() == HttpStatus.OK) {
                responseBody = responseTemplate.getBody();

                // 쿠키 생성 및 설정
                Cookie cookie = new Cookie("jwt", responseBody.get("access_token").toString());
                cookie.setHttpOnly(true);      // JavaScript 접근 불가
                cookie.setPath("/");           // 모든 경로에 적용
                cookie.setMaxAge(Integer.parseInt(String.valueOf(responseBody.get("expires_in"))));

                Cookie mailCookie = new Cookie("email", email);
                mailCookie.setHttpOnly(true);      // JavaScript 접근 불가
                mailCookie.setPath("/");           // 모든 경로에 적용
                mailCookie.setMaxAge(Integer.parseInt(String.valueOf(responseBody.get("expires_in"))));

                response.addCookie(cookie);
                response.addCookie(mailCookie);

                return responseBody.get("access_token").toString();
            }

        } catch (Exception e)
        {
            log.error(e.toString());
        }

        return "";
    }

    public List<AuthUser> user(HttpServletRequest request,
                        HttpServletResponse response)
    {
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

        List<AuthUser> body = new ArrayList<>();

        HttpEntity<List<AuthUser> > requestEntity = new HttpEntity<>(body, headers);

        try {

            ResponseEntity< List<AuthUser>> responseTemplate = authTemplate.exchange("/user", HttpMethod.POST, requestEntity, new ParameterizedTypeReference< List<AuthUser>>() {});

            return responseTemplate.getBody();


        } catch (Exception e)
        {
            log.error(e.toString());
        }

        return null;

    }
}
