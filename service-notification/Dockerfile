FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl service-notification -am -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/service-notification/target/service-notification-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE  8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]