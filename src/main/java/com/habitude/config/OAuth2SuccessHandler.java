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
import java.util.Map;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();
        
        // Get user info from OAuth provider
        Map<String, Object> attributes = oauthUser.getAttributes();
        String email = (String) attributes.get("email");
        
        // Generate a token
        String token = "oauth-token-" + email;
        
        // Simple HTML that will close the popup and send token to parent
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Login Success</title>
            </head>
            <body>
                <script>
                    window.opener.postMessage({ token: '%s' }, 'http://localhost:8081');
                    window.close();
                </script>
            </body>
            </html>
            """.formatted(token);
        
        response.setContentType("text/html");
        response.getWriter().write(html);
    }
}