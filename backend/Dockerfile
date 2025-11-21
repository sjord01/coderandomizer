# Build stage: use official maven image (includes mvn)
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only what is needed for dependency download first (cache-friendly)
COPY pom.xml ./
COPY .mvn .mvn
# Copy source
COPY src ./src

# Build the project (skip tests for faster builds; remove -DskipTests to run them)
RUN mvn -B -DskipTests package

# Runtime stage: smaller JRE image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar from the build stage (adjust name if your artifactId/version differs)
COPY --from=build /app/target/coderandomizer-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]