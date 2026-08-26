FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY project/ ./project
RUN mvn clean package -f project/pom.xml -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/project/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]