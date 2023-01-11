#copy to root
FROM amazoncorretto:11-alpine-jdk
EXPOSE 8090
ARG JAR_FILE=target/storyteller-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]