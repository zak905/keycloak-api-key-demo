#FROM maven:3-openjdk-11-slim as builder

#RUN mkdir app

#WORKDIR app

#COPY src src
#COPY pom.xml .

#RUN mvn clean package

FROM openjdk:17-alpine

ARG VERSION=0.1

COPY ./target/dashboard-service-${VERSION}.jar app.jar

EXPOSE 8180

CMD ["java", "-jar", "app.jar"]