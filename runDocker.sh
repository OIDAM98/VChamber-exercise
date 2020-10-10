#!/bin/bash

echo "Starting docker"

sbt docker:publishLocal

docker images | grep "chamber-tests"

docker run chamber-tests

dockerId=$(docker ps -a --filter ancestor=chamber-tests -q)

echo "Docker id: $dockerId"

echo "Removing $dockerId"
docker rm "$dockerId"

echo "Cleaning local Docker repository"
sbt docker:clean