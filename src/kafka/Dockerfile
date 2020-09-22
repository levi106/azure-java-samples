FROM mcr.microsoft.com/java/jdk:11u8-zulu-alpine as builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build
FROM mcr.microsoft.com/java/jfe-headless:11u8-zulu-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/kafka-0.0.1-SNAPSHOT.jar ./app.jar
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.0.0-PREVIEW.5/applicationinsights-agent-3.0.0-PREVIEW.5.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx128M", "-XX:+PrintFlagsFinal", "-javaagent:/app/applicationinsights-agent-3.0.0-PREVIEW.5.jar", "-jar", "app.jar"]