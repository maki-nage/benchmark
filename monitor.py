import subprocess
import time
import statistics


def consumer_groups():
    res = subprocess.check_output(
        "docker exec -it benchmark_kafka_1 "
        "kafka-consumer-groups --describe "
        "--group makinage_benchmark "
        "--bootstrap-server localhost:9092 ",
        shell=True
    )

    return res.decode("utf-8")


def compute_total_lag(stats):
    stats = stats.split('\n')[2:-1]
    total_lag = 0
    for line in stats:
        line = line.split()
        total_lag += int(line[5])

    return total_lag


def main():
    step = 0
    rps = []
    prev_total_lag = -1
    while True:
        time.sleep(10.0)
        stats = consumer_groups()
        total_lag = compute_total_lag(stats)
        if total_lag == 0:
            break

        if step >= 2:
            rate = (prev_total_lag - total_lag) / 10.0
            rps.append(rate)
        prev_total_lag = total_lag
        step += 1

    if len(rps) > 0:
        print(int(statistics.mean(rps)))
    else:
        print(0)


if __name__ == '__main__':
    main()
