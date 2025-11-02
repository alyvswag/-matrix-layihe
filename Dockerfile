FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN  ./mvnw clean package -DskipTests

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]