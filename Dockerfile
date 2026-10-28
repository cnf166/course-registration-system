# Build stage: compile the fat jar with JDK 21
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY libs libs
COPY src src

# Cache ~/.m2 across builds so dependencies aren't re-downloaded on every source change
RUN --mount=type=cache,target=/root/.m2 \
    chmod +x mvnw && ./mvnw -B -q package -DskipTests && cp target/*.jar app.jar

# Run stage: only the JRE and the jar
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/app.jar app.jar

ENV DAO_SQLITE_PATH=/app/data/app.db
VOLUME /app/data
EXPOSE 8080

# SQLite JDBC loads native code, so allow native access to avoid JDK warnings
ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "-jar", "app.jar"]
