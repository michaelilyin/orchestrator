FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app

RUN apk --no-cache add ca-certificates tzdata libcap
RUN setcap cap_net_raw=+ep ${JAVA_HOME}/bin/java

ENV TZ Europe/Moscow

COPY ./build/libs/*.jar /app/status-check.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/status-check.jar"]
