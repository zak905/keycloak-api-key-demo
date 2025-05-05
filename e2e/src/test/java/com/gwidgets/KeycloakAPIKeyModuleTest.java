package com.gwidgets;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


// tests the whole stack of keycloak-api-key-demo
// requires the stack is in https://github.com/zak905/keycloak-api-key-demo/blob/master/docker-compose.yaml
// before running: docker compose up
public class KeycloakAPIKeyModuleTest {

    @Test
    public void e2e() throws IOException, InterruptedException {
        String dashboarServicedUrl = System.getProperty("dashboardServiceUrl", "http://localhost:8180");
        String restServicedUrl = System.getProperty("restServiceUrl", "http://localhost:8280");
        String keycloakRealm = System.getProperty("keycloakRealm", "example");
        String keycloakBaseUrl = System.getProperty("keycloakBaseUrl", "http://auth-server:8080");
        String headless = System.getProperty("headless", "true");

        ChromeOptions options = new ChromeOptions();
        if (headless.equals("true")) {
            options.addArguments("--headless");
        }
        WebDriver driver = new ChromeDriver(options);

        driver.navigate().to(dashboarServicedUrl);

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));

        var registerLink = driver.findElement(By.linkText("Register"));
        registerLink.click();

        WebElement emailInput = driver.findElement(By.id("email"));
        emailInput.sendKeys("e2e-test"+UUID.randomUUID().toString()+"@test.com");
        var passwordInput = driver.findElement(By.id("password"));
        var password = UUID.randomUUID().toString();
        passwordInput.sendKeys(password);
        var passwordConfirmInput = driver.findElement(By.id("password-confirm"));
        passwordConfirmInput.sendKeys(password);
        var firstNameInput = driver.findElement(By.id("firstName"));
        firstNameInput.sendKeys("tester");
        var lastNameInput = driver.findElement(By.id("lastName"));
        lastNameInput.sendKeys("tester");

        var registerButton = driver.findElement(By.cssSelector("input[type=submit]"));
        registerButton.click();

        String apiKey = driver.findElement(By.id("api-key")).getText();

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        var unauthenticatedRequest = HttpRequest.newBuilder()
                .uri(URI.create(restServicedUrl))
                .GET()
                .build();

        var response = client.send(unauthenticatedRequest, HttpResponse.BodyHandlers.discarding());

        assertEquals(response.statusCode(), 401);

        var authenticatedRequest = HttpRequest.newBuilder()
                .uri(URI.create(restServicedUrl))
                  .header("x-api-key", apiKey)
                .GET()
                .build();

        response = client.send(authenticatedRequest, HttpResponse.BodyHandlers.discarding());

        assertEquals(response.statusCode(), 200);

        var logout = driver.findElement(By.id("logout"));
        logout.click();

        Thread.sleep(1000L);

        // we are back to the login
        assertEquals(driver.getTitle(), "Sign in to " + keycloakRealm);
        var currentUrl = URI.create(driver.getCurrentUrl());
        var currentBaseUrl = currentUrl.getScheme() + "://" + currentUrl.getAuthority();
        assertEquals(currentBaseUrl, keycloakBaseUrl);

        // attempt to go back to the dashboard after logout
        driver.navigate().to(dashboarServicedUrl);
        // this sleep is needed, otherwise driver.getCurrentUrl(), returns previous url, weird ?
        Thread.sleep(1000L);

        currentUrl = URI.create(driver.getCurrentUrl());
        currentBaseUrl = currentUrl.getScheme() + "://" + currentUrl.getAuthority();

        // to test that we are effectively logged out
        // we check we are redirected to the login
        // if we try to navigate
        assertEquals(currentBaseUrl, keycloakBaseUrl);

        driver.quit();
    }
}
