# Étape 1 : Build avec Maven
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -Pprod -DskipTests

# Étape 2 : Exécution avec OpenJDK
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


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