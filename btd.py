#!/usr/bin/env python
#
# btd.py
#
# Library to parse battery tracer btd files (sqlite databases)
#
# Copyright 2012 Motorola Mobility
#
# Author: Rodd Zurcher <rodd.zurcher@motorola.com>
#

try:
    from pysqlite2 import dbapi2 as sqlite
except ImportError:
    import sqlite3 as sqlite

import re
from datetime import datetime, timedelta
from numbers import Number
import traceback


def parse_minmax(db, table='t_fgdata', fields=None):
    """
    Opens a sqlite database db.
    Returns a dictionary for each field in fields with the found
    MIN and MAX value
    """
    if fields is None:
        fields = ['FCC', 'TEMP', 'TEMP_1']

    data = {}
    con = None
    try:
        con = sqlite.connect(db)
        cur = con.cursor()

        select = 'SELECT MIN(%s),MAX(%s) FROM %s'

        for f in fields:
            cur.execute(select % (f, f,  table))
            row = cur.fetchone()
            data[f] = {'min': row[0], 'max': row[1]}

    except sqlite.Error, e:
        raise ValueError(e.args[0])

    if con:
        con.close()

    return data


def fix_rx_tx(o, prev):
    """
    Modifies row dict o in place, fixing following rx/tx issues:
    1. btd has rx and tx swapped
    2. rx and tx are signed 32 bit ints -> rollovers

    Keeps track of current offsets in new field named 'rx_tx_offsets'.

    Note that we are only ever interested in deltas between rx/tx values
    from different rows, as opposed to their absolute values, so we make
    no effort to compute correct absolute values.

    """

    # pick offsets from prev object, or start with default of 0
    if prev:
        offsets = prev['rx_tx_offsets']
    else:
        offsets = {'CELL_RX': 0, 'CELL_TX': 0, 'WIFI_RX': 0, 'WIFI_TX': 0}

    o['rx_tx_offsets'] = offsets

    # apply offsets and swap rx and tx values
    o['CELL_RX'], o['CELL_TX'] = (o['CELL_TX'] + offsets['CELL_RX'],
                                  o['CELL_RX'] + offsets['CELL_TX'])

    o['WIFI_RX'], o['WIFI_TX'] = (o['WIFI_TX'] + offsets['WIFI_RX'],
                                  o['WIFI_RX'] + offsets['WIFI_TX'])

    # check for rollover
    # note that setting threshold at 75% of rollover should catch
    # all rollovers without catching resets of field to 0
    if prev:
        rollover = 2**32
        threshold = rollover / 4 * 3

        for k in ('CELL_RX', 'CELL_TX', 'WIFI_RX', 'WIFI_TX'):
            if o[k] - prev[k] > threshold:
                offsets[k] -= rollover
                o[k] -= rollover



def populate_local_time(o, prev, tzoffset):
    """
    Calculates localtime and inserts it into dict row dict o.

    o and prev are assumed to be the current and previous row dicts
    from btd file, scanned from the bottom up. (prev is None at
    the start, when o points to the bottom-most row).

    tzoffset is timezone offset in seconds.

    We use value of localtime derived for prev in order to calculate
    localtime for o. We try to use RealTimeTotal whenever we can,
    since this field is not subject to network time
    adjustments. However, RealTimeTotal occasionally gets reset, so we
    detect that and use timestamp offset instead. Note that timestamp
    *is* subject to localtime adjustments, however the hope is that this
    adjustment will not coincide with RealTimeTotal being reset.
    """

    o['localtime_raw'] = ( datetime.utcfromtimestamp(o['timestamp']/1000.) +
                           timedelta(seconds=tzoffset) )
    if prev is None:
        # previous record missing, trust the timestamp
        o['localtime'] = o['localtime_raw']
    elif prev['RealTimeTotal'] < o['RealTimeTotal']:
        # can't use RealTimeTotal from previous record since
        # framework started new discharge period. Use delta between
        # timestamps instead to offset the time
        #print 'RealTimeTotal was reset'
        o['localtime'] = (
            prev['localtime'] +
            timedelta(seconds=(o['timestamp'] - prev['timestamp'])/1000.))
    else:
        # best case scenario: use RealTimeTotal delta to adjust the time
        o['localtime'] = (
            prev['localtime'] +
            timedelta(seconds=(o['RealTimeTotal'] - prev['RealTimeTotal'])/1000.))
        # DEBUG: calculate relative difference between two ways of calculating time
        diff1 = (o['localtime'] - prev['localtime']).total_seconds()
        diff2 = (o['timestamp'] - prev['timestamp']) / 1000.
        reldiff = (diff1 - diff2) / max(abs(diff1), abs(diff2))
        # print reldiff
        # if abs(reldiff) > .05: print 'warning: delta = ', diff1 - diff2, 'time = ', o['localtime']




def dict_factory(cursor, row):
    """
    Sqlite row factory returning rows as dicts. Using this instead of the
    more efficient sqlite3.Row so that resulting rows are writable (e.g.
    values can be changed, new keys added, etc.)
    """
    return dict((col[0], val) for col, val in zip(cursor.description, row))


def to_float(s):
    """
    Sad sad little hack to convert f into a float even if the decimal
    point is a comma and not a period.
    """
    return float(s.replace(',', '.'))


MS_MULTIPLIERS = {
    'ms': 1,
    's': 1000,
    'm': 60000,
    'h': 3600000,
    'd': 86400000
}
def convert_time(s):
    """Converts string such as '1m,18s,559ms' into int number of ms"""
    s = s.strip()
    tot = 0
    for x in s.split(','):
        val, unit = re.match(r'(\d+)(\S+)', x).groups()
        tot += int(val) * MS_MULTIPLIERS[unit]
    return tot


def convert_time_ignore_rest(s):
    time_part = re.match(r'\s*([\dmshd,]*)', s).group(1)
    return convert_time(time_part) if time_part else 0


def convert_perc(s):
    """Converts string such as '(12.7%)' into float number of percents"""
    return to_float(re.search(r'\(([\d.,]*)%\)', s).group(1))


name_time_percent_count_re = \
    re.compile(r"""(?P<name>\S+)
                   \s+(?P<time>.*?)
                   \s\((?P<percent>[0-9.,]+)%\)
                   \s*(?P<count>\d*)x?,?\s*""", re.VERBOSE)

def convert_ntpc(s):
    """
    Split a string that's a list of name/time/percent/count chunks
    into a dictionaries keyed by name field

    Returns a dictionary
    """
    s = s.strip()

    res = {}
    index = 0

    while index < len(s):
        m = name_time_percent_count_re.match(s, index)
        if m:
            d = m.groupdict()
            d['time'] = convert_time(d['time'])
            d['percent'] = to_float(d['percent'])
            if len(d['count']) == 0:
                del d['count']
            else:
                d['count'] = int(d['count'])
            res[d['name']] = d
            index = m.end()
        else:
            raise ValueError('invalid name/time/percent/count match: %s' % s)
    return res


def build_ntpc_converter(empty_match):
    """
    Returns version of ntpc converter (based on convert_ntpc() function) which
    first checks if passed-in string matches empty_match regexp, and if it does,
    returns an empty dict without invoking convert_ntpc.
    Returns converter function.
    """
    def converter(s):
        return {} if empty_match.search(s) else convert_ntpc(s)

    return converter

convert_ntpc_no_activity = build_ntpc_converter(re.compile(r'No activity'))

CONVERSIONS = {
    'RealTimeTotal': convert_time,
    'AwakeTimeTotal': convert_time,
    'RealTimeOnBatt': convert_time_ignore_rest,
    'AwakeTimeOnBatt': convert_time_ignore_rest,
    'ScreenOnTime': convert_time_ignore_rest,
    'ActivePhoneCallTime': convert_time_ignore_rest,
    'ScreenBrightnesses': convert_ntpc_no_activity,
    'WifiRunningTime': convert_time_ignore_rest,
    'WifiOnTime': convert_time_ignore_rest,
    'BluetoothOnTime': convert_time_ignore_rest,
    'SignalLevels': convert_ntpc_no_activity,
    'RadioTypes': convert_ntpc_no_activity,
}


def convert_row(row_dict):
    """
    Takes a row dict from btd file, and converts bunch of fields to
    friendlier units, modifying the dict in place.
    Returns None.
    """
    for key, converter in CONVERSIONS.iteritems():
        val = row_dict.get(key)
        if val is not None:
            try:
                row_dict[key] = converter(val)
            except Exception, e:
                msg = 'Field conversion failure, k {}, v {}, rowid {}\n{}'.format(
                    key, repr(val), row_dict['rowid'], traceback.format_exc())
                raise ValueError(msg)


def iter_btd_backwards(conn):
    "Iterate over btd rows bottom up"
    cur = conn.execute('select rowid, * from t_fgdata order by rowid desc')
    res = cur.fetchmany(100)
    while res:
        for row in res:
            yield row
        res = cur.fetchmany()


def is_nighttime(dt):
    """
    Calculates if time falls in the night.
    parameters:
        dt - datetime object
    returns: True if 11pm < dt < 7am, False otherwise
    """
    return dt.hour >= 23 or dt.hour < 7


def dictdiff(d1, d2, fields=[], ignore=[]):
    """
    Calculate "delta" between two dicts (d2 - d1).

    Delta is calculated for numeric, datetime, timedelta, and (recursively)
    for dict fields.

    If passed in, "fields" should be a list of field names (dict keys) to
    calculate deltas for. The default is to consider all keys.

    If passed in, "ignore" should be a list of field names to ignore.

    Keys and value types on dict d2 controll for which fields delta will be
    calculated. When dict d1 has matching keys, then the corresponding values
    should be of the same type as in dict d2, or failure may occur.

    Missing numeric/dict fields in d1 are defaulted to 0/{}, while missing
    datetime/timedelta fields are simply ignored.

    Returns dict of the same shape as d1/d2, but with the subset of keys,
    plus additional fields named '<field>_start' and '<field>_end' holding
    corresponding fields' values from d1 and d2.
    """

    d = {}
    # by default get diff over all keys
    if not fields: fields = [k for k in d2.keys() if not k in ignore]

    for f in fields:
        v2 = d2.get(f)
        if isinstance(v2, Number):
            v1 = d1.get(f, 0)
            d[f] = v2 - v1
        elif isinstance(v2, datetime) or isinstance(v2, timedelta):
            v1 = d1.get(f)
            if v1 is not None:
                d[f] = v2 - v1
        elif isinstance(v2, dict):
            v1 = d1.get(f, {})
            d[f] = dictdiff(v1, v2, ignore=ignore)
        d[f + '_start'] = d1.get(f)
        d[f + '_end'] = d2.get(f)
    return d


def process_btd(conn):
    """
    Find and return info about all discharge intervals in the btd file.
    Parameter conn must be sqlite db connection.
    Returns dictionary with fcc_min/max and discharge data.
    """

    BATTERY_STATUS_DISCHARGING = 3
    PLUG_TYPE_NONE = 0
    DIFF_FIELDS = ['ActivePhoneCallTime',
                   'AwakeTimeOnBatt',
                   'AwakeTimeTotal',
                   'BATTERY_LEVEL',
                   'BluetoothOnTime',
                   'CELL_RX',
                   'CELL_TX',
                   'MovementCount',
                   'MovementCountQD',
                   'RadioTypes',
                   'RealTimeOnBatt',
                   'RealTimeTotal',
                   'ScreenBrightnesses',
                   'ScreenOnTime',
                   'SignalLevels',
                   'TimeInMovement',
                   'TimeInMovementUnstowed',
                   'TimeStowed',
                   'WIFI_RX',
                   'WIFI_TX',
                   'WifiOnTime',
                   'WifiRunningTime']

    discharge_periods = []

    def add_disch(s, lst):
        d = dictdiff(s.start, s.end, fields=DIFF_FIELDS, ignore=['percent'])
        d['start_time'] = s.start['localtime']
        d['end_time'] = s.end['localtime']
        d['nt_dur'] = s.nt_dur
        d['nt_screen_time'] = s.nt_screen
        d['nt_discharge'] = s.nt_disch
        d['disch_son'] = s.d_son
        d['time_son'] = s.t_son
        d['disch_soff'] = s.d_soff
        d['time_soff'] = s.t_soff
        d['disch_mixed'] = s.d_mixed
        d['time_mixed'] = s.t_mixed
        lst.append(d)

    def reset_stats(s):
        # start and end of discharge period
        s.start = s.end = None

        # night time duration, screen time, and discharge percentage
        s.nt_dur, s.nt_screen, s.nt_disch = 0, 0, 0

        # screen on/off/mixed discharge amounts and times
        s.d_son, s.d_soff, s.d_mixed = 0, 0, 0
        s.t_son, s.t_soff, s.t_mixed = 0, 0, 0

    # install our own row factory
    conn.row_factory = dict_factory

    cursor = conn.execute(
        'select VALUE from t_phoneinfo where name="TZ_CURRENT_OFFSET" limit 1')
    tz_offset = int(cursor.fetchone()['VALUE']) / 1000.


    # battery capacity min and max
    fcc_min, fcc_max = float('+inf'), float('-inf')

    # object holding stats for the current discharge period
    class O(object): pass
    s = O()

    reset_stats(s)

    # count of ignored rows in the btd file
    ignored_rows = 0

    prev = None
    # NOTE: we are going backwards in time, from more recent to less recent time
    for curr in iter_btd_backwards(conn):

        # ignore rows with invalid battery stats (failure to connect to service)
        if (curr['SignalLevels'] == 'Unknown' or curr['RadioTypes'] == 'Unknown'
            or curr['ScreenBrightnesses'] == 'Unknown'):
            ignored_rows += 1
            continue

        convert_row(curr)
        populate_local_time(curr, prev, tz_offset)
        fix_rx_tx(curr, prev)

        fcc_min = min(fcc_min, curr['FCC'])
        fcc_max = max(fcc_max, curr['FCC'])

        if (curr['BATTERY_STATUS'] == BATTERY_STATUS_DISCHARGING and
            curr['PLUG_TYPE'] == PLUG_TYPE_NONE):
            if s.end is None:
                # Mark the end of discharge period. (Since we are iterating
                # bottom up, the "end" really means "beginning" from our
                # sick, inverted perspective.)
                # This variable also serves as the indicator that at least one
                # discharge point has already been encountered in this interval.
                s.end = curr
            else:
                delta_ms = prev['RealTimeTotal'] - curr['RealTimeTotal']
                if delta_ms < 0:
                    # RealTimeTotal got reset in the middle of a discharge
                    # period. Emit the data for the previous discharge
                    # interval, and start a new one.
                    if s.start: add_disch(s, discharge_periods)
                    reset_stats(s)
                    s.end = curr
                else:
                    # update all stats for this discharge period
                    delta_charge = curr['BATTERY_LEVEL'] - prev['BATTERY_LEVEL']
                    delta_screen_time = prev['ScreenOnTime'] - curr['ScreenOnTime']
                    s.start = curr
                    # collect nighttime stats if both points fall in the night
                    if (is_nighttime(curr['localtime']) and
                        is_nighttime(prev['localtime']) and
                        delta_ms < 8 * 1000 * 3600):
                        s.nt_dur += delta_ms
                        s.nt_screen += delta_screen_time
                        s.nt_disch += delta_charge

                    # Assign the discharged amount between screen buckets.
                    # Sadly, the screen_on cumulative time is tracked with only
                    # 1 second resolution. We assume that the screen was on during
                    # the entire time between the two sample points if increase
                    # in ScreenOnTime agrees with time span within 1 sec. We assume
                    # that screen was off during the entire interval if ScreenOnTime
                    # increased by less than 1 sec. The rest we assign to the
                    # "mixed" screen bucket (i.e. screen was part on, part off).
                    if delta_screen_time < 1000:
                        s.d_soff += delta_charge
                        s.t_soff += delta_ms
                    elif abs(delta_screen_time - delta_ms) < 1000:
                        s.d_son += delta_charge
                        s.t_son += delta_ms
                    else:
                        s.d_mixed += delta_charge
                        s.t_mixed += delta_ms

        elif s.end:
            # no longer discharging, but have some preceeding discharge data
            if s.start:
                add_disch(s, discharge_periods)
            reset_stats(s)

        prev = curr

    if s.start and s.end:
        add_disch(s, discharge_periods)

    return {
        'FCC_min': fcc_min,
        'FCC_max': fcc_max,
        'tz_offset': tz_offset,
        'discharges': discharge_periods,
        'ignored_rows': ignored_rows
    }


def process_btd_file(fname):
    conn = sqlite.connect(fname)
    return process_btd(conn)


if __name__ == '__main__':
    import sys

    def json_encoder(obj):
        "json encoder which encodes datetime objects as strings"
        if isinstance(obj, datetime):
            return str(obj)
        else:
            raise TypeError('Type not JSON seralizable: %s' % type(obj))

    try:
        import json
    except ImportError:
        import simplejson as json

    for fname in sys.argv[1:]:
        report = process_btd_file(fname)
        json.dump(report, sys.stdout, indent=2, sort_keys=True,
                  default=json_encoder)
        sys.stdout.write('\n')
