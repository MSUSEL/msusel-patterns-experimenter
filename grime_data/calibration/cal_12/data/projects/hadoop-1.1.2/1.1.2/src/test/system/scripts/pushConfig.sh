#!/usr/bin/env bash
#
# The MIT License (MIT)
#
# MSUSEL Arc Framework
# Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
# Software Engineering Laboratory and Idaho State University, Informatics and
# Computer Science, Empirical Software Engineering Laboratory
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#

# local folder with new configuration file
LOCAL_DIR=$1
# remote daemon host
HOST=$2
#remote dir points to the location of new config files
REMOTE_DIR=$3
# remote daemon HADOOP_CONF_DIR location
DAEMON_HADOOP_CONF_DIR=$4

if [ $# -ne 4 ]; then
  echo "Wrong number of parameters" >&2
  exit 2
fi

ret_value=0

echo The script makes a remote copy of existing ${DAEMON_HADOOP_CONF_DIR} to ${REMOTE_DIR}
echo and populates it with new configs prepared in $LOCAL_DIR

ssh ${HOST} cp -r ${DAEMON_HADOOP_CONF_DIR}/* ${REMOTE_DIR}
ret_value=$?

# make sure files are writeble
ssh ${HOST} chmod u+w ${REMOTE_DIR}/*

# copy new files over
scp -r ${LOCAL_DIR}/* ${HOST}:${REMOTE_DIR}

err_code=`echo $? + $ret_value | bc`
echo Copying of files from local to remote returned ${err_code}

