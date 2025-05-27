FROM maven:3.9.9-openjdk-21 AS build
COPY ..
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/best_Store_SB-0.01-SNAPSHOT.jar best_Store_SB.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","best_Store_SB.jar"]
