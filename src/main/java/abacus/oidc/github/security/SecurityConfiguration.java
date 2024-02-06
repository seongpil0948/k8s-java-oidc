package abacus.oidc.github.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {
                AuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
                http.oauth2Login(o -> o.failureHandler((request, response, exception) -> {
                        request.getSession().setAttribute("error.message", exception.getMessage());
                        handler.onAuthenticationFailure(request, response, exception);
                        response.sendRedirect("/unauthenticated");
                }));
                http
                                .oauth2Client()
                                .and()
                                .oauth2Login()
                                .tokenEndpoint()
                                .and()
                                .userInfoEndpoint();
                http
                                .sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

                http
                                .authorizeHttpRequests()
                                .requestMatchers("/unauthenticated", "/oauth2/**", "/login/**").permitAll()
                                .anyRequest()
                                .fullyAuthenticated()
                                .and()
                                .logout()
                                .logoutSuccessUrl(
                                                "http://localhost:8080/realms/external/protocol/openid-connect/logout?redirect_uri=http://localhost:8081/");

                return http.build();
        }
}