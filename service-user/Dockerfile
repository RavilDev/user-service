FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl service-user -am -DskipTests

FROM openjdk:21
WORKDIR /app
COPY --from=build /app/service-user/target/service-user-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE  8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]