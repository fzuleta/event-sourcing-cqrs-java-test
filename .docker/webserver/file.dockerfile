FROM openjdk:alpine

MAINTAINER Felipe Zuleta 
ARG mainDir=webserver
ARG mainJar=webserver.jar

COPY ./Server/$mainDir/target/$mainJar /opt/$mainJar
COPY ./.docker/$mainDir/$env/firstrun.sh /
COPY ./.docker/config.properties /opt/event-source

# Copy aurelia stuff ------------------------------------------------
COPY ./client/dice/index.html /opt/aurelia/
COPY ./web/scripts /opt/aurelia/scripts
# -------------------------------------------------------------------

EXPOSE 8888 8443

WORKDIR /opt/

RUN apk update && \
    apk upgrade && \
    apk add nano && \
    apk add --update bash && \
    rm -rf /var/cache/apk/* && \
    chmod +x /firstrun.sh && \
    chmod +x /opt/$mainJar

ENTRYPOINT ["/firstrun.sh"]