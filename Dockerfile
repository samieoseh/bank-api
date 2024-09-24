# Maven
FROM maven:3.9.9-sapmachine-22 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# JDK
FROM openjdk:24-slim-bullseye
WORKDIR /app
COPY --from=build /app/target/bankapi-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]