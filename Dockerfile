FROM maven:3.6.1-jdk-14

RUN mkdir /app
WORKDIR /app

# Download dependencies once, not every build
COPY pom.xml pom.xml
RUN mvn dependency:resolve compile package

COPY src src
RUN mvn compile package
RUN ls
CMD ["java", "-jar", "target/auction-challenge-0.1.0-SNAPSHOT-jar-with-dependencies.jar"]
