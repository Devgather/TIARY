FROM eclipse-temurin:11-jre-alpine

COPY build/libs/*.jar application.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=development", "application.jar"]