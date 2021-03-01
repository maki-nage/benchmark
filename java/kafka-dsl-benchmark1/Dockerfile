FROM openjdk:8-jre

ADD ./target/kafka-dsl-benchmark1-*-with-dependencies.jar /usr/src/kafka-dsl-benchmark1.jar
WORKDIR /usr/src
ENTRYPOINT exec java -jar /usr/src/kafka-dsl-benchmark1.jar ${APPLICATION_ID}
