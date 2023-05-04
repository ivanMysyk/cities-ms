FROM maven:3.9.1-amazoncorretto-17 AS maven
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn clean install -DdisableIntTests=true

CMD mvn spring-boot:run -Dspring-boot.run.profiles=container