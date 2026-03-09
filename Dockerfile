FROM eclipse-temurin:21-jre

WORKDIR /app

ARG SERVICE_NAME

COPY ${SERVICE_NAME}/target/${SERVICE_NAME}.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]