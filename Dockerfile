### BUILD stage
FROM maven:3-jdk-11 as builder
RUN mkdir -p /build
WORKDIR /build
COPY ./myth-rest-api/pom.xml /build
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY ./myth-rest-api/src /build/src
RUN mvn -f /build/pom.xml clean package -DskipTests

### RUN stage
FROM adoptopenjdk/openjdk11:alpine-jre as runtime
ENV APP_HOME /app
RUN mkdir $APP_HOME
WORKDIR $APP_HOME
ARG JAR_FILE=/build/target/myth-rest-api-0.0.1-SNAPSHOT.jar
COPY --from=builder ${JAR_FILE} app.jar
EXPOSE 8443

ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]