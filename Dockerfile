FROM openjdk:21
LABEL authors="Lawrence Ching"

WORKDIR /app

COPY target/soj-1.0-SNAPSHOT.jar /app/soj.jar
COPY *.soj .

RUN chmod +x *.soj

ENTRYPOINT ["./Hello.soj"]