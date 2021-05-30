FROM maven:3.6.3-jdk-8-slim AS build
COPY src home/src
COPY pom.xml home/pom.xml
RUN mvn -f home/pom.xml clean install -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=build /home/target/movie-collection-application*.jar movie-collection-application.jar
COPY --from=build /home/src/main/resources/static/photos /photos
ENV MEDIASERVICE_PATH=/photos/
ENTRYPOINT ["java", "-jar", "/movie-collection-application.jar"]
