FROM gradle:5.6.4-jdk8 AS build

LABEL maintainer="me@stefanreinke.com"

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon 

FROM openjdk:8-jre-slim

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/apiservice.jar

EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/app/apiservice.jar"]