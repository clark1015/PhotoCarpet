FROM eclipse-temurin:11-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=./build/libs/photocarpet-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]