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
#currently we're embedding the final output

cd ..
docker-compose up -d
