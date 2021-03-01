#! /bin/sh

echo "Initializing datasets and benchmark..."
python3 create_dataset.py
docker build -t makinage-benchmark ./python
docker-compose up -d kafka
sleep 10
docker exec -it benchmark_kafka_1 /tmp/inject_dataset.sh

echo "running python-bench1 single node..."
docker-compose up -d makinage-benchmark
python3 monitor.py -g makinage_benchmark
docker-compose stop makinage-benchmark

echo "running python-bench1 with 5 nodes..."
docker-compose up --scale makinage-benchmark=5 -d makinage-benchmark
python3 monitor.py -g makinage_benchmark
docker-compose stop makinage-benchmark

echo "stopping benchmark services..."
docker-compose down -v
