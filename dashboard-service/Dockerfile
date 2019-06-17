FROM maven:3.6-jdk-8-alpine as builder

RUN mkdir app

WORKDIR app

COPY src src
COPY pom.xml .

RUN mvn clean package

FROM openjdk:8-jre-alpine

ARG VERSION=0.1

COPY --from=builder /app/target/dashboard-service-${VERSION}.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]