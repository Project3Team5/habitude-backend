FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/habitude-backend-1.0.0.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java","-jar","app.jar"]
