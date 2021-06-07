FROM maven:3.5-jdk-8-alpine as maven
ARG MAVEN_OPTS
WORKDIR /app
COPY . /app/
RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:8
WORKDIR /app
COPY --from=maven /app/target/projectmanagerapi-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
CMD ["java","-jar","projectmanagerapi-0.0.1-SNAPSHOT.jar"]
