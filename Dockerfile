#FROM maven:3.9.1-amazoncorretto-17 AS maven
#WORKDIR /usr/src/app
#COPY . /usr/src/app
#RUN mvn package
#
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY --from=maven /usr/src/app/target/spring-boot-api-cities.jar /app/
#CMD ["java", "-jar", "/app/spring-boot-api-cities.jar"]\

FROM maven:3.9.1-amazoncorretto-17 AS maven
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn clean install

CMD mvn spring-boot:run