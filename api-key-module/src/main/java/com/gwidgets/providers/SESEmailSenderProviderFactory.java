package com.gwidgets.providers;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import java.util.Objects;
import org.keycloak.Config.Scope;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.email.EmailSenderProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class SESEmailSenderProviderFactory implements EmailSenderProviderFactory {

  private static AmazonSimpleEmailService sesClientInstance;

  @Override
  public EmailSenderProvider create(KeycloakSession session) {
    //using singleton pattern to avoid creating the client each time create is called
    if (sesClientInstance == null) {
      String awsRegion = Objects.requireNonNull(System.getenv("AWS_REGION"));

      sesClientInstance =
          AmazonSimpleEmailServiceClientBuilder
              .standard().withCredentials(new EnvironmentVariableCredentialsProvider())
              .withRegion(awsRegion)
              .build();
    }

    return new SESEmailSenderProvider(sesClientInstance);
  }

  @Override
  public void init(Scope config) {}

  @Override
  public void postInit(KeycloakSessionFactory factory) {}

  @Override
  public void close() {}

  @Override
  public String getId() {
    return "default";
  }
}
