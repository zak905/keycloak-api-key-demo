#!/bin/bash


url=$1
max_tries=$2
sleep_time=$3

declare -i tries

until $(curl --silent --output /dev/null --head --fail $url); do
    echo "waiting for service $url to be up"
    (( tries++ ))
     echo "try number $tries"
    sleep $sleep_time
    if [ $tries -eq $max_tries ]; then
        exit 1
    fi
done

