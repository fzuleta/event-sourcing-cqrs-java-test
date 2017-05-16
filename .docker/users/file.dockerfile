FROM openjdk:alpine

MAINTAINER Felipe Zuleta 
ARG mainDir=users
ARG mainJar=users.jar

COPY ./server/$mainDir/target/$mainJar /opt/$mainJar
COPY ./.docker/$mainDir/firstrun.sh /
COPY ./.docker/config.properties /opt/event-source/

WORKDIR /opt/

RUN apk update && \
    apk upgrade && \
    apk add nano && \
    apk add --update bash && \
    rm -rf /var/cache/apk/* && \
    chmod +x /firstrun.sh && \
    chmod +x /opt/$mainJar

ENTRYPOINT ["/firstrun.sh"]