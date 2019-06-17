package com.gwidgets;

import javax.servlet.http.HttpServletRequest;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {


  @GetMapping(value = "/user", produces = "application/json")
  @ResponseBody
  public AccessToken getUserInfo(HttpServletRequest request) {
    return ((KeycloakPrincipal<RefreshableKeycloakSecurityContext>)((KeycloakAuthenticationToken)request.getUserPrincipal()).getPrincipal()).getKeycloakSecurityContext().getToken();
  }

  @PostMapping("/logout")
  public @ResponseBody void logout(HttpServletRequest request) throws Exception {
    request.logout();
  }
}
