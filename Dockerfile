FROM openjdk:11
ADD myth-rest-api/target/myth-rest-api-0.0.1-SNAPSHOT.jar myth-rest-api-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "myth-rest-api-0.0.1-SNAPSHOT.jar"]