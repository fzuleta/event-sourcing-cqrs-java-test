FROM openjdk:8-jdk-alpine

MAINTAINER Felipe Zuleta 
ARG mainDir=eventstore
ARG mainJar=eventstore.jar

COPY ./server/$mainDir/target/$mainJar /opt/$mainJar
COPY ./.docker/$mainDir/firstrun.sh /
COPY ./.docker/config.properties /opt/event-source/
COPY ./.docker/$mainDir/osql /opt/orientdb/osql

WORKDIR /opt/

RUN apk update && \
    apk upgrade && \
    apk add nano && \
    apk add --update bash && \
    rm -rf /var/cache/apk/* && \
    chmod +x /firstrun.sh && \
    chmod +x /opt/$mainJar

ENTRYPOINT ["/firstrun.sh"]