FROM openjdk:11-jdk-slim

WORKDIR /src
COPY . /src

RUN apt-get update
RUN apt-get install -y dos2unix
RUN dos2unix gradlew

RUN bash gradlew fatJar

WORKDIR /run
RUN cp /src/build/libs/*.jar /run/example-0.0.1.jar

EXPOSE 8080

CMD java -jar /run/example-0.0.1.jar
