FROM ubuntu:latest
LABEL authors="elhadjimamadousarr"

RUN mvn clean package -Pprod -DskipTests

FROM openjdk:23-jdk-slim
COPY target/*.jar app.jar

COPY --from=build target/portfolio-0.0.1-SNAPSHOT.jar portfolio.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]

#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["top", "-b", "java", "-jar", "/app.jar"]




