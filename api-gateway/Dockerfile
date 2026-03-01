FROM eclipse-temurin:17-alpine-3.23 AS runtime

WORKDIR /app

ARG JAR_FILE=target/api-gateway-*.jar
COPY ${JAR_FILE} app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
