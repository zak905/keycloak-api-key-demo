FROM java:8-jre-alpine

ENV KEYCLOAK_VERSION 6.0.1

RUN apk add --no-cache ca-certificates openssl && wget https://downloads.jboss.org/keycloak/${KEYCLOAK_VERSION}/keycloak-${KEYCLOAK_VERSION}.tar.gz

RUN tar xvf keycloak-${KEYCLOAK_VERSION}.tar.gz && rm keycloak-${KEYCLOAK_VERSION}.tar.gz

WORKDIR keycloak-${KEYCLOAK_VERSION}

#add admin user
RUN ./bin/add-user-keycloak.sh -u admin -p admin --realm master

COPY target/api-key-ear-0.1.ear standalone/deployments

EXPOSE 8080

ENTRYPOINT ["./bin/standalone.sh", "-b", "0.0.0.0"]