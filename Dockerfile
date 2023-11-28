FROM openjdk:17-alpine

RUN apk add tzdata

ARG JAR_FILE=/build/libs/main-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /main.jar

ENTRYPOINT ["java","-jar", "/main.jar"]
