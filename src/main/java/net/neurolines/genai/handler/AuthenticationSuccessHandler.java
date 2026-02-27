package net.neurolines.genai.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

   /*     // 권한
        String role = null;
        if (principal instanceof TbMember) {
            role = ((TbMember) principal).getRole(); // UserDetails에서 사용자 이름 추출
        }else if ("anonymousUser".equals(principal)) {
            role = "USER"; // 익명 사용자 처리
        }
        else {
            role = principal.toString(); // principal이 문자열일 경우
        }*/

        response.sendRedirect("/pages/dashboard");
    }
}

