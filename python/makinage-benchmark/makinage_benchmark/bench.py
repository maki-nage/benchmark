import json
from datetime import datetime
from collections import namedtuple

import rx
import rxsci as rs
import dateutil.parser


Feature = namedtuple('Feature', ['event_id', 'event_time', 'group_id', 'field2_mean'])


def encoder():
    ''' Custom Json encoder

    encodes items using the JSON format, and uses iso8601 for event_time field.
    '''
    def _encode(i):
        return json.dumps(i._replace(
            event_time=i.event_time.isoformat()
        )._asdict()).encode()

    def _decode(i):
        i = json.loads(i.decode())
        i['event_time'] = dateutil.parser.isoparse(i['event_time'])
        return i

    return _encode, _decode


def clip_hour():
    return rs.ops.map(lambda i: i._replace(
        event_time=datetime(
            year=i.event_time.year, month=i.event_time.month, day=i.event_time.day,
            hour=i.event_time.hour
        )
    ))


def compute_hourly_mean(config, data):
    feature = data.pipe(
        rs.state.with_memory_store(
            rs.ops.group_by(lambda i: i['group_id'], pipeline=rx.pipe(
                rs.data.split(lambda i: i['event_time'].hour, pipeline=rx.pipe(
                    rs.ops.tee_map(
                        rs.ops.last(),
                        rx.pipe(
                            rs.ops.map(lambda i: i['data']['field2']),
                            rs.math.mean(reduce=True),
                        )
                    ),
                    rs.ops.map(lambda i: Feature(
                        event_id=i[0]['event_id'],
                        event_time=i[0]['event_time'],
                        group_id=i[0]['group_id'],
                        field2_mean=i[1],
                    )),
                    clip_hour(),
                )),
            )),
        ),
    )

    return feature,
