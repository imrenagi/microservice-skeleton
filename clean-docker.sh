#!/bin/bash

docker rm $(docker ps -a -q) -f
docker rmi $(docker images -q) -f