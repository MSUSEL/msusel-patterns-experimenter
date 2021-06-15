#!/bin/bash
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

# First we need to determine whether Failmon has been distributed with
# Hadoop, or as standalone. In the latter case failmon.jar will lie in
# the current directory.

JARNAME="failmon.jar"
HADOOPDIR=""
CLASSPATH=""

if [ `ls -l | grep src | wc -l` == 0 ]
then
    # standalone binary
    if [ -n $1 ] && [ "$1" == "--mergeFiles" ]
    then
	jar -ufe $JARNAME org.apache.hadoop.contrib.failmon.HDFSMerger
        java -jar $JARNAME
    else
    	jar -ufe $JARNAME org.apache.hadoop.contrib.failmon.RunOnce
	java -jar $JARNAME $*
    fi
else
    # distributed with Hadoop
    HADOOPDIR=`pwd`/../../../
    CLASSPATH=$CLASSPATH:$HADOOPDIR/build/contrib/failmon/classes
    CLASSPATH=$CLASSPATH:$HADOOPDIR/build/classes
    CLASSPATH=$CLASSPATH:`ls -1 $HADOOPDIR/lib/commons-logging-api-1*.jar`
    CLASSPATH=$CLASSPATH:`ls -1 $HADOOPDIR/lib/commons-logging-1*.jar`
    CLASSPATH=$CLASSPATH:`ls -1 $HADOOPDIR/lib/log4j-*.jar`
#    echo $CLASSPATH
    if [ -n $1 ] && [ "$1" == "--mergeFiles" ]
    then
        java -cp $CLASSPATH org.apache.hadoop.contrib.failmon.HDFSMerger
    else
        java -cp $CLASSPATH org.apache.hadoop.contrib.failmon.RunOnce $*
    fi
fi

