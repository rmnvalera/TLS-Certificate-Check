FROM maven:3.6.1-jdk-11 AS builder
WORKDIR /project
COPY . /project
RUN mvn clean package

FROM openjdk:11
WORKDIR /app/
COPY config/config.yml /app/config/config.yml
COPY file.db /app/
COPY --from=builder /project/target/TLS-Certificate-Check-1.0.jar /app/
ENTRYPOINT [ "java", "-jar", "TLS-Certificate-Check-1.0.jar" ]