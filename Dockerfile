FROM openjdk:21
LABEL authors="Lawrence Ching"

WORKDIR /app

COPY bin/sj /usr/bin/sj
COPY target/sj-1.0-SNAPSHOT.jar /app/sj.jar
COPY ./examples ./examples
COPY ./Test.sj ./Test.sj
RUN chmod +x /usr/bin/sj
RUN chmod +x *.sj
