# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Development stage (Java 21 JDK, runs prebuilt JAR)
FROM eclipse-temurin:21-jdk AS dev
WORKDIR /app
COPY --from=build /workspace/target/foodlabel-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Production stage (Java 21 JRE)
FROM eclipse-temurin:21-jre AS prod
WORKDIR /app
COPY --from=build /workspace/target/foodlabel-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
