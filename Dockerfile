FROM ubuntu:latest
LABEL authors="elhadjimamadousarr"

FROM openjdk:23-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["top", "-b", "java", "-jar", "/app.jar"]

#COPY . .
#
#
#
#FROM eclipse-temurin:17-jdk-alpine
#COPY --from=build target/portfolio-0.0.1-SNAPSHOT.jar portfolio.jar
#ENV SPRING_PROFILES_ACTIVE=prod
#EXPOSE 8080