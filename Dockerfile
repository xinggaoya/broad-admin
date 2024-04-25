FROM openjdk:latest

WORKDIR /app

COPY broad-admin/target/broad-admin-2.0.0.jar /app/broad-admin-2.0.0.jar

CMD ["java", "-jar", "/app/broad-admin-2.0.0.jar"]
