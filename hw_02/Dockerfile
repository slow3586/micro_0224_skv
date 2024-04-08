FROM amazoncorretto:17-alpine
VOLUME /tmp
COPY target/*.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/app.jar"]