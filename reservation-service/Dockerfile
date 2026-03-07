# ===== BUILD STAGE =====
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -e -B dependency:go-offline

COPY src ./src
RUN mvn -q -e -B clean package -DskipTests

# ===== RUNTIME STAGE =====
FROM gcr.io/distroless/java17-debian12:nonroot

WORKDIR /app
COPY --from=build /app/target/reservation-service-*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
