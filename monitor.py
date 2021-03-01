import subprocess
import time
import statistics
import sys
import getopt


def consumer_groups(group_id):
def consumer_groups():
    res = subprocess.check_output(
        "docker exec -it benchmark_kafka_1"
        " kafka-consumer-groups --describe"
        " --group " + group_id +
        " --bootstrap-server localhost:9092 ",
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


def main(argv):
    step = 0
    rps = []
    prev_total_lag = -1
    group_id = ''

    try:
        opts, args = getopt.getopt(argv,"hg:",["groupid="])
    except getopt.GetoptError:
        print('monitor.py -g <groupid>')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('monitor.py -g <groupid>')
            sys.exit()
        elif opt in ("-g", "--groupid"):
            group_id = arg

    print('Group id minitored: ' + group_id)

    while True:
        time.sleep(10.0)
        stats = consumer_groups(group_id)
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
    main(sys.argv[1:])
