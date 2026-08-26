FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY project/ ./project
RUN mvn clean package -f project/pom.xml -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/project/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]