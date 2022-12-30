#FROM maven:3.8.2-jdk-11
#
#WORKDIR ../myth-rest-api
#COPY myth-rest-api .
#RUN mvn clean install

#CMD mvn spring-boot:run
#
#
FROM openjdk:11
ADD myth-rest-api/target/myth-rest-api-0.0.1-SNAPSHOT.jar myth-rest-api-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dockerembbed", "-jar", "myth-rest-api-0.0.1-SNAPSHOT.jar"]