#!/usr/bin/env bash
myFile="/firstrun.exists"
if [ -e "$myFile" ]; then
    sleep 5

else
    touch "$myFile"
    sleep 5
fi

cd /opt
java -jar apigateway.jar