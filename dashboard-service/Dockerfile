FROM maven:3-openjdk-11-slim as builder

RUN mkdir app

WORKDIR app

COPY src src
COPY pom.xml .

RUN mvn clean package

FROM openjdk:11-jre-slim

ARG VERSION=0.1

COPY --from=builder /app/target/dashboard-service-${VERSION}.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]