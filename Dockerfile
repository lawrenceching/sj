FROM openjdk:21
LABEL authors="Lawrence Ching"

WORKDIR /root

COPY bin/sj /usr/bin/sj
COPY target/sj-1.0-SNAPSHOT.jar /etc/sj/sj.jar
COPY examples/Hello.sj Hello.sj
RUN chmod +x /usr/bin/sj
