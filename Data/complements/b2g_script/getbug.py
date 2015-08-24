# Copyright 2012 Motorola Mobility
"""Downloads bug2go zip files by bugid."""

from __future__ import print_function
import sys
import os
import re
import argparse
import getpass

import requests

__author__ = "Rodd Zurcher <rzurcher@motorola.com>"
__version__ = '1.3'
B2G = 'https://b2gadm-mcloud101-blur.svcmot.com/bugreport/report'


def main():
    # warn if requests version is < 1.0.0
    if requests.__version__ < '1.0.0':
        print("Warning: requests version: {0} < 1.0.0. See README.md".
              format(requests.__version__))
    # grab the current user for the default
    user = getpass.getuser()
    parser = argparse.ArgumentParser(
        description=__doc__,
        formatter_class=argparse.ArgumentDefaultsHelpFormatter)

    parser.add_argument('--user', default=user,
                        help='User to login as')
    auth_group = parser.add_mutually_exclusive_group()
    auth_group.add_argument('--passwd',
                            help='Password to use. Default: prompt')
    auth_group.add_argument(
        '--authfile',
        type=argparse.FileType('r'),
        help='Path to a file containing authentication credentials.; Files '
             'should contain 2 lines. The 1st line username, the 2nd line; '
             'password')
    parser.add_argument('--overwrite', action='store_true',
                        help='overwrite download file if it exists')
    parser.add_argument('--verbose', '-v', action='store_true',
                        help='Enable logging of the http request')
    parser.add_argument('bugid', nargs='+', help="bugid to download")
    parser.add_argument(
        '--version', '-V', action='version',
        version='%(prog)s {version}'.format(version=__version__))

    args = parser.parse_args()

    if args.verbose:
        import httplib as http_client
        import logging

        http_client.HTTPConnection.debuglevel = 1
        logging.basicConfig()
        logging.getLogger().setLevel(logging.DEBUG)
        requests_log = logging.getLogger("requests.packages.urllib3")
        requests_log.setLevel(logging.DEBUG)
        requests_log.propagate = True

    # load user and password from authfile
    if args.authfile:
        args.user = args.authfile.readline().rstrip()
        args.passwd = args.authfile.readline().rstrip()
        args.authfile.close()
        if args.user == '' or args.passwd == '':
            sys.stderr.write("Authfile did not contain 2 lines.\n")
            parser.print_usage()
            sys.exit(1)

    # prompt for password if none provided
    if args.passwd is None:
        args.passwd = getpass.getpass()

    # session to hold the login cookie
    session = requests.session()

    # post login credentials, content contains 'Invalid' if credentials
    # are wrong
    result = session.post(
        B2G + '/verify.action',
        data={'username': args.user, 'password': args.passwd},
        allow_redirects=False)
    if 'Invalid' in result.content:
        print("Login failed")
        sys.exit(1)

    # Enable a stream kwarg to post if request version >= 1.0.0
    requests_kwargs = {}
    if requests.__version__ >= '1.0.0':
        requests_kwargs['stream'] = True

    # Download all the bugs
    for bug in args.bugid:
        result = session.post(
            B2G + '/downloadlog.action',
            data={'bg_id': bug}, allow_redirects=False,
            **requests_kwargs)
        if result.status_code != requests.codes.ok:
            print("Download of {0} failed.".format(bug))
            continue

        print("Tamanho: " + result.headers["Content-Length"])

        # the post succeeds even if the bug is not found to download
        # confirm a content-disposition exists
        if 'Content-Disposition' not in result.headers:
            print("Bug {0} was not found".format(bug))
            continue

        # Extract a filename to save as
        match = re.search(
            'filename="(.*)"$', result.headers['Content-Disposition'])
        if match:
            filename = match.group(1)
        else:
            filename = str(bug) + ".zip"
        print("Saving {0} as {1}".format(bug, filename))
        if os.path.exists(filename) and not args.overwrite:
            print("Download of {0} skipped, file {1} exists".
                  format(bug, filename))
            continue
        with open(filename, "wb") as outfile:
            if requests.__version__ >= '1.0.0':
                for chunk in result.iter_content(chunk_size=32768):
                    outfile.write(chunk)
            else:
                outfile.write(result.content)


if __name__ == '__main__':
    main()
