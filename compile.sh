#!/bin/bash

if [ $? -ne 0 ]; then
    echo "Failed to compile the services. Use 'all' or the name of services!"
    exit
fi

DIR=$PWD

if [ $1 == "all" ]; then
    cd ${DIR}/config && mvn clean package
    cd ${DIR}/gateway && mvn clean package
    cd ${DIR}/monitoring && mvn clean package
    cd ${DIR}/registry && mvn clean package
    cd ${DIR}/service-account && mvn clean package
else
    for var in "$@"
    do
        cd ${DIR}/$var && mvn clean package
    done
fi

