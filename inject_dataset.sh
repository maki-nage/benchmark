#! /bin/sh

kafka-console-producer --broker-list localhost:9092 --topic bench1_feature --property parse.key=true --property key.separator=, < /tmp/dataset.txt