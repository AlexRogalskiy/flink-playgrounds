
FROM maven:3.6-jdk-8-slim AS builder

# Get Click Count job and compile it
COPY ./java/flink-playground-clickcountjob /opt/flink-playground-clickcountjob
WORKDIR /opt/flink-playground-clickcountjob
RUN mvn clean install


###############################################################################
# Build Operations Playground Image
###############################################################################

FROM apache/flink:1.12.1-scala_2.11-java8

WORKDIR /opt/flink/bin

# Copy Click Count Job
COPY --from=builder /opt/flink-playground-clickcountjob/target/flink-playground-clickcountjob-*.jar /opt/DeliveryWriteFileJob.jar
