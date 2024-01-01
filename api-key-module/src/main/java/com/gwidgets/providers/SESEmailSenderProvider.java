package com.gwidgets.providers;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.util.Map;
import org.jboss.logging.Logger;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.models.UserModel;

public class SESEmailSenderProvider implements EmailSenderProvider {

  private static final Logger log = Logger.getLogger("org.keycloak.events");

  private final AmazonSimpleEmailService sesClient;

  public SESEmailSenderProvider(
      AmazonSimpleEmailService sesClient) {
    this.sesClient = sesClient;
  }

  @Override
  public void send(Map<String, String> config, UserModel user, String subject, String textBody,
      String htmlBody) throws EmailException {
    this.send(config, user.getEmail(), subject, textBody, htmlBody);
  }

  @Override
  public void send(Map<String, String> config, String address, String subject, String textBody, String htmlBody) throws EmailException {
    log.info("attempting to send email using aws ses for " + address);

    Message message = new Message().withSubject(new Content().withData(subject))
        .withBody(new Body().withHtml(new Content().withData(htmlBody))
            .withText(new Content().withData(textBody).withCharset("UTF-8")));

    SendEmailRequest sendEmailRequest = new SendEmailRequest()
        .withSource("example<" + config.get("from") + ">")
        .withMessage(message).withDestination(new Destination().withToAddresses(address));

    sesClient.sendEmail(sendEmailRequest);
    log.info("email sent to " + address + " successfully");
  }

  @Override
  public void close() {}
}
