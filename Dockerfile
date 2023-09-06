FROM ubuntu:latest
FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=local", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]