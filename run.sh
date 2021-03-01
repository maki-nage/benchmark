#! /bin/sh

echo "Initializing datasets and benchmark..."
python3 create_dataset.py
docker build -t makinage-benchmark ./python

(cd ./java/kafka-dsl-benchmark1 &&  mvn clean install -U)
docker build -t kafka-dsl-benchmark1 ./java/kafka-dsl-benchmark1

docker-compose up -d kafka
sleep 10

docker exec -it benchmark_kafka_1 /tmp/inject_dataset.sh

echo "running python-bench1 single node..."
docker-compose up -d makinage-benchmark
python3 monitor.py -g makinage_benchmark
docker-compose stop makinage-benchmark

echo "running kafka-dsl-bench1 single node..."
docker-compose up -d kafka-dsl-benchmark1
python3 monitor.py -g kafka-dsl-benchmark1
docker-compose stop kafka-dsl-benchmark1


echo "running python-bench1 with 5 nodes..."
docker-compose up --scale makinage-benchmark=5 -d makinage-benchmark
python3 monitor.py -g makinage_benchmark
docker-compose stop makinage-benchmark

echo "running kafka-dsl-bench1(-scaled) with 5 nodes..."
docker-compose up --scale kafka-dsl-benchmark1-scaled=5 -d kafka-dsl-benchmark1-scaled
python3 monitor.py -g kafka-dsl-benchmark1-scaled
docker-compose stop kafka-dsl-benchmark1-scaled

echo "stopping benchmark services..."
docker-compose down -v
