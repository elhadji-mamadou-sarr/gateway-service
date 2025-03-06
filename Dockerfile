FROM ubuntu:latest
LABEL authors="elhadjimamadousarr"

FROM openjdk:23-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["top", "-b", "java", "-jar", "/app.jar"]