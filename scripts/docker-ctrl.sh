#!/bin/bash

ACTION=$1

if [ "$ACTION" = "start" ]; then
  echo "Building and starting Docker containers..."
  docker-compose down --volumes --rmi all --remove-orphans
  docker-compose up --build -d
elif [ "$ACTION" = "stop" ]; then
  echo "Stopping containers and removing images, volumes..."
  docker-compose down --volumes --rmi all --remove-orphans
else
  echo "Usage: ./docker-control.sh [start|stop]"
fi
