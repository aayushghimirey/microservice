FROM eclipse-temurin:21-jre

WORKDIR /app

ARG SERVICE_NAME

# Copy the built jar of the specific service
COPY ${SERVICE_NAME}/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]