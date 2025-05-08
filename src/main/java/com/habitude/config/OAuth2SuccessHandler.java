package com.habitude.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.habitude.model.User;
import com.habitude.repository.UserRepository;
import com.habitude.dto.UserResponseDto;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public OAuth2SuccessHandler(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();

        String email = (String) oauthUser.getAttributes().get("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after OAuth login"));

        UserResponseDto userDto = new UserResponseDto(user);
        String json = objectMapper.writeValueAsString(userDto);

        String html = """
            <!DOCTYPE html>
            <html>
            <head><title>Login Success</title></head>
            <body>
                <script>
                    window.opener.postMessage(%s, 'http://localhost:8081');
                    window.close();
                </script>
            </body>
            </html>
            """.formatted(json);

        response.setContentType("text/html");
        response.getWriter().write(html);
    }
}