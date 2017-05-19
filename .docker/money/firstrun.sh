#!/usr/bin/env bash
myFile="/firstrun.exists"
if [ -e "$myFile" ]; then
    echo "Waiting 10 seconds to give some time to eventstore to be setup"
    sleep 10

else
    touch "$myFile"
    echo "Waiting 30 seconds to give some time to eventstore to be setup"
    sleep 30
fi

cd /opt
java -jar money.jar