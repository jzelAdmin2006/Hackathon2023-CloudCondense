# Build
FROM gradle:jdk21 AS build
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Serve
FROM eclipse-temurin:21

RUN apt update && apt install -y p7zip-full

ENV tmpArchiveWorkDir=/work

WORKDIR /app

COPY --from=build /app/build/libs/app.jar .
ENTRYPOINT ["java","-jar","./app.jar"]
EXPOSE 8080
