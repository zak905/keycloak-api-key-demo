FROM openjdk:11-jre-slim


WORKDIR /keycloak

ENV KEYCLOAK_VERSION 12.0.4

RUN apt-get update && apt-get install -y wget && wget https://github.com/keycloak/keycloak/releases/download/${KEYCLOAK_VERSION}/keycloak-${KEYCLOAK_VERSION}.tar.gz

RUN tar xvf keycloak-${KEYCLOAK_VERSION}.tar.gz  --strip-components=1 && rm keycloak-${KEYCLOAK_VERSION}.tar.gz

#add admin user
RUN ./bin/add-user-keycloak.sh -u admin -p admin --realm master

COPY target/api-key-ear-0.1.ear standalone/deployments

EXPOSE 8080

ENTRYPOINT ["./bin/standalone.sh", "-b", "0.0.0.0"]