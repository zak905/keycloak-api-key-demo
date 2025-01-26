package com.gwidgets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    String keycloakBaseUrl;

    Logger log = Logger.getLogger("com.gwidgets.SecurityConfiguration");

  @Bean
   SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
     return httpSecurity.authorizeHttpRequests((authorize) -> {
       authorize.requestMatchers("/**").authenticated();
     }).anonymous((anonymousConfigurer) -> anonymousConfigurer.disable()).csrf((csrfConfigurer) -> csrfConfigurer.disable()).
             sessionManagement((sessionManagementConfigurer) -> {
               sessionManagementConfigurer.addSessionAuthenticationStrategy(new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl()));
             })
             .oauth2Login(Customizer.withDefaults())
             .logout((configurer) -> {
                    configurer.logoutUrl("/logout");
                    configurer.logoutSuccessHandler((req, res, authentication) -> {
                        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                        DefaultOidcUser defaultOidcUser = (DefaultOidcUser) token.getPrincipal();

                        HttpRequest request = HttpRequest.newBuilder().GET()
                                .uri(URI.create(keycloakBaseUrl + "/protocol/openid-connect/logout?" +
                                        "id_token_hint="+defaultOidcUser.getIdToken().getTokenValue()))
                                .build();
                        HttpClient client = HttpClient.newBuilder().build();
                        HttpResponse response;
                        try {
                            response = client.send(request, HttpResponse.BodyHandlers.discarding());
                            if (response.statusCode() != 200) {
                                log.warning("Keycloak logout failed, received status: " + response.statusCode());
                            } else {
                                log.info("logout from Keycloak successfull");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        res.sendRedirect("/");
                    });
             })
              .build();
  }
}
