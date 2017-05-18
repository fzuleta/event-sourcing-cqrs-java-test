#!/bin/bash 
#Goes to the same directory
cd $(dirname $0)

#first build shared libs
cd server/sharedlibs
mvn clean install

#build server
cd ..
cd ..
cd server
mvn clean package

#build client
#todo
#currently we're embedding the final output

cd ..

#Remove any running docker containers
docker rm -f -v fz_apigateway
docker rm -f -v fz_webserver
docker rm -f -v fz_money
docker rm -f -v fz_users
docker rm -f -v fz_eventstore

# remove any images to start fresh
docker rmi fz/apigateway:latest
docker rmi fz/webserver:latest
docker rmi fz/money:latest
docker rmi fz/users:latest
docker rmi fz/eventstore:latest

docker-compose up --build -d
