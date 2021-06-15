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

# Set HdfsProxy-specific environment variables here.

# The only required environment variable is JAVA_HOME.  All others are
# optional.  When running a distributed configuration it is best to
# set JAVA_HOME in this file, so that it is correctly defined on
# remote nodes.

# The java implementation to use.  Required.
# export JAVA_HOME=/usr/lib/j2sdk1.5-sun

# Extra Java CLASSPATH elements.  Optional.
# export HDFSPROXY_CLASSPATH=

# The maximum amount of heap to use, in MB. Default is 1000.
# export HDFSPROXY_HEAPSIZE=2000

# Extra Java runtime options.  Empty by default.
# export HDFSPROXY_OPTS=

# Extra ssh options.  Empty by default.
# export HDFSPROXY_SSH_OPTS="-o ConnectTimeout=1 -o SendEnv=HDFSPROXY_CONF_DIR"

# Where log files are stored.  $HDFSPROXY_HOME/logs by default.
# export HDFSPROXY_LOG_DIR=${HDFSPROXY_HOME}/logs

# File naming remote slave hosts.  $HDFSPROXY_HOME/conf/slaves by default.
# export HDFSPROXY_SLAVES=${HDFSPROXY_HOME}/conf/slaves

# host:path where hdfsproxy code should be rsync'd from.  Unset by default.
# export HDFSPROXY_MASTER=master:/home/$USER/src/hdfsproxy

# Seconds to sleep between slave commands.  Unset by default.  This
# can be useful in large clusters, where, e.g., slave rsyncs can
# otherwise arrive faster than the master can service them.
# export HDFSPROXY_SLAVE_SLEEP=0.1

# The directory where pid files are stored. /tmp by default.
# export HDFSPROXY_PID_DIR=/var/hdfsproxy/pids

# A string representing this instance of hdfsproxy. $USER by default.
# export HDFSPROXY_IDENT_STRING=$USER

# The scheduling priority for daemon processes.  See 'man nice'.
# export HDFSPROXY_NICENESS=10
