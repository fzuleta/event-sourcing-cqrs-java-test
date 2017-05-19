#!/usr/bin/env bash
myFile="/firstrun.exists"
if [ -e "$myFile" ]; then
    sleep 5

else
    mkdir orientdb
    mkdir orientdb/eventstore
    chmod -R 755 orientdb
    touch "$myFile"
    sleep 5
fi

cd /opt

JAVA_OPTS_MEMORY="-Xms2G -Xmx2G"
JAVA_OPTS_SCRIPT="-Djna.nosys=true -XX:+HeapDumpOnOutOfMemoryError -XX:MaxDirectMemorySize=512g -Djava.awt.headless=true -Dfile.encoding=UTF8 -Drhino.opt.level=9"

echo $JAVA_OPTS_MEMORY
echo $JAVA_OPTS_SCRIPT

java -jar \
    $JAVA_OPTS_MEMORY \
    $JAVA_OPTS_SCRIPT \
    eventstore.jar
