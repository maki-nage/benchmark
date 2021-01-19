FROM makinage/makinage:latest-pypy


COPY makinage-benchmark /opt/makinage-benchmark/
COPY bench.yml /opt/makinage/config.yaml
WORKDIR /opt/makinage-benchmark

RUN python3 setup.py install
