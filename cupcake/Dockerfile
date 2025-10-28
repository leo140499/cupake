FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY pom.xml ./
RUN ./mvnw dependency:go-offline
COPY . ./
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
