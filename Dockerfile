FROM openjdk:21-jdk-slim
LABEL authors="vital_goncharov"

WORKDIR /app

COPY ./IRTSU_CAS-0.0.1-SNAPSHOT.jar /app/IRTSU_CAS-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/IRTSU_CAS-0.0.1-SNAPSHOT.jar"]