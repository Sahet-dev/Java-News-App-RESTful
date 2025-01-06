#FROM  (jdk should be defined here)
FROM eclipse-temurin:21-jdk AS base
ARG JAR_FILE=target/newsBackend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
