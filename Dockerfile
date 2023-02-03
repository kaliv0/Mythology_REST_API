FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=myth-rest-api/target/myth-rest-api-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8443
ENTRYPOINT ["java","-jar","app.jar"]