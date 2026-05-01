FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
RUN mvn install -N -DskipTests
COPY car-service/pom.xml ./car-service/pom.xml
COPY car-service/src ./car-service/src
RUN mvn -f car-service/pom.xml package spring-boot:repackage -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/car-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
