import uuid
import json
import random
from datetime import datetime, timedelta
from collections import namedtuple


Data = namedtuple('Data', ['field1', 'field2', 'field3'])
Header = namedtuple('Feature', ['event_id', 'event_time','group_id', 'data'])


def create_feature_generator(item_count, group_count):
    uuids = []
    start_date = datetime(year=2020, month=7, day=12)
    groups_time_offsets = {}
    for _ in range(group_count):
        uid = str(uuid.uuid4())
        uuids.append(uid)
        groups_time_offsets[uid] = 0
    while item_count > 0:
        uid = random.choice(uuids)
        d = {'field1': random.randrange(100), 'field2': random.random(), 'field3': True}
        f = {
            'event_id': item_count, 
            'event_time': (start_date + timedelta(minutes=groups_time_offsets[uid])).isoformat(),
            'group_id': uid, 'data': d
        }
        groups_time_offsets[uid] += 13
        yield f
        item_count -= 1


def main():
    gen = create_feature_generator(10000000, 100000)
    with open('dataset.txt', 'w') as fd:
        for utterance in gen:
            fd.write("{},{}\n".format(
                utterance['group_id'],
                json.dumps(utterance)
            ))


if __name__ == '__main__':
    main()
