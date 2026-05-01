FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./pom.xml
RUN mvn install -N -DskipTests
COPY user-profile-service/pom.xml ./user-profile-service/pom.xml
COPY user-profile-service/src ./user-profile-service/src
RUN mvn -f user-profile-service/pom.xml package spring-boot:repackage -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/user-profile-service/target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
