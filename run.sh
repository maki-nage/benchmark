#! /bin/sh

python3 create_dataset.py
docker build -t makinage-benchmark ./python
docker-compose up -d kafka
sleep 10
docker exec -it benchmark_kafka_1 /tmp/inject_dataset.sh
docker-compose up -d makinage-benchmark
#docker-compose up --scale makinage-benchmark=5 -d