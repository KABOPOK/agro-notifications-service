FROM --platform=linux/arm64 eclipse-temurin:21-jre
WORKDIR /app
COPY target/notifications-service-0.0.1-SNAPSHOT.jar notifications-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "notifications-service-0.0.1-SNAPSHOT.jar"]