package com.gwidgets;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class DashboardController {

  @GetMapping(value = "/user", produces = "application/json")
  @ResponseBody
  public Map <String, Object> getUserInfo(HttpServletRequest request) {
    var tokenAttributes = ((OAuth2AuthenticationToken) request.getUserPrincipal()).getPrincipal().getAttributes();
    return Map.of("api-key", tokenAttributes.get("api-key"), "email", tokenAttributes.get("email"));
  }
}
