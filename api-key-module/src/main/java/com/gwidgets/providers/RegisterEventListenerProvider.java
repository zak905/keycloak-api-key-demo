package com.gwidgets.providers;


import jakarta.persistence.EntityManager;
import org.keycloak.common.util.SecretGenerator;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.jpa.entities.UserAttributeEntity;
import org.keycloak.models.jpa.entities.UserEntity;

import java.util.Objects;
import java.util.UUID;

public class RegisterEventListenerProvider implements EventListenerProvider  {
    //keycloak utility to generate random strings, anything can be used e.g. UUID,...
    private final SecretGenerator secretGenerator;
    private final EntityManager entityManager;

    public RegisterEventListenerProvider(KeycloakSession session) {
        this.entityManager = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        this.secretGenerator = SecretGenerator.getInstance();
    }

    public void onEvent(Event event) {
        //we are only interested in the register event
        if (event.getType().equals(EventType.REGISTER)) {
            String userId = event.getUserId();
            addApiKeyAttribute(userId);
        }
    }

    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        // in case the user is created from admin or rest api
        if (Objects.equals(adminEvent.getResourceType(), ResourceType.USER) && Objects.equals(adminEvent.getOperationType(), OperationType.CREATE)) {
            String userId = adminEvent.getResourcePath().split("/")[1];
            if (Objects.nonNull(userId)) {
                addApiKeyAttribute(userId);
            }
        }
    }



    public void addApiKeyAttribute(String userId) {
        String apiKey = secretGenerator.randomString(50);
        UserEntity userEntity = entityManager.find(UserEntity.class, userId);
        UserAttributeEntity attributeEntity = new UserAttributeEntity();
        attributeEntity.setName("api-key");
        attributeEntity.setValue(apiKey);
        attributeEntity.setUser(userEntity);
        attributeEntity.setId(UUID.randomUUID().toString());
        entityManager.persist(attributeEntity);
    }

    @Override
    public void close() {}
}
