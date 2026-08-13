package by.bsuir.tcs.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "Неверный логин или пароль";

        Throwable cause = exception;
        if (exception instanceof InternalAuthenticationServiceException) {
            cause = exception.getCause();
        }

        if (cause instanceof DisabledException) {
            errorMessage = "Ваша учетная запись заблокирована. Обратитесь к администратору.";
        }

        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8)
                .replace("+", "%20");

        setDefaultFailureUrl("/login?error=" + encodedMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}