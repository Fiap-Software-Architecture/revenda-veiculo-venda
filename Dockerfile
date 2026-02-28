FROM amazoncorretto:21.0.9-alpine3.22

USER root

WORKDIR /app

COPY target/venda-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8082
