#!/usr/bin/env bash
myFile="/firstrun.exists"
if [ -e "$myFile" ]; then
    sleep 3

else
    touch "$myFile"
    sleep 3
fi

cd /opt
java -jar money.jar