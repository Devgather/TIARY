FROM eclipse-temurin:11-jre-alpine

RUN apk --no-cache add tzdata && cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo "Asia/Seoul" > /etc/timezone && apk del tzdata

COPY build/libs/*.jar application.jar

ENTRYPOINT ["java", "-jar", "-Djava.net.preferIPv4Stack=true", "-Dspring.profiles.active=production", "application.jar"]