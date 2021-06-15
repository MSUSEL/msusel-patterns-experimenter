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


# Run a shell command on all slave hosts.
#
# Environment Variables
#
#   HDFSPROXY_SLAVES    File naming remote hosts.
#     Default is ${HDFSPROXY_CONF_DIR}/hdfsproxy-hosts.
#   HDFSPROXY_CONF_DIR  Alternate conf dir. Default is ${HDFSPROXY_HOME}/conf.
#   HDFSPROXY_SLAVE_SLEEP Seconds to sleep between spawning remote commands.
#   HDFSPROXY_SSH_OPTS Options passed to ssh when running remote commands.
##

usage="Usage: hdfsproxy-slaves.sh [--config confdir] command..."

# if no args specified, show usage
if [ $# -le 0 ]; then
  echo $usage
  exit 1
fi

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

. "$bin"/hdfsproxy-config.sh

# If the slaves file is specified in the command line,
# then it takes precedence over the definition in 
# hdfsproxy-env.sh. Save it here.
HOSTLIST=$HDFSPROXY_SLAVES

if [ -f "${HDFSPROXY_CONF_DIR}/hdfsproxy-env.sh" ]; then
  . "${HDFSPROXY_CONF_DIR}/hdfsproxy-env.sh"
fi

if [ "$HOSTLIST" = "" ]; then
  if [ "$HDFSPROXY_SLAVES" = "" ]; then
    export HOSTLIST="${HDFSPROXY_CONF_DIR}/hdfsproxy-hosts"
  else
    export HOSTLIST="${HDFSPROXY_SLAVES}"
  fi
fi

for slave in `cat "$HOSTLIST"`; do
 ssh $HDFSPROXY_SSH_OPTS $slave $"${@// /\\ }" \
   2>&1 | sed "s/^/$slave: /" &
 if [ "$HDFSPROXY_SLAVE_SLEEP" != "" ]; then
   sleep $HDFSPROXY_SLAVE_SLEEP
 fi
done

wait
