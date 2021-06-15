#!/bin/sh
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

this="$0"
while [ -h "$this" ]; do
  ls=`ls -ld "$this"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    this="$link"
  else
    this=`dirname "$this"`/"$link"
  fi
done

# convert relative path to absolute path
bin=`dirname "$this"`
script=`basename "$this"`
bin=`cd "$bin"; pwd`
this="$bin/$script"

# Check if HADOOP_HOME AND JAVA_HOME is set.
if [ -z "$HADOOP_HOME" ] && [ -z "$HADOOP_PREFIX" ] ; then
  echo "HADOOP_HOME or HADOOP_PREFIX environment variable should be defined"
  exit -1;
fi

if [ -z "$JAVA_HOME" ] ; then
  echo "JAVA_HOME environment variable not defined"
  exit -1;
fi

if [ -z "$HADOOP_PREFIX" ]; then
  hadoopVersion=`$HADOOP_HOME/bin/hadoop version | awk 'BEGIN { RS = "" ; FS = "\n" } ; { print $1 }' | awk '{print $2}'`
else
  hadoopVersion=`$HADOOP_PREFIX/bin/hadoop version | awk 'BEGIN { RS = "" ; FS = "\n" } ; { print $1 }' | awk '{print $2}'`
fi

# so that filenames w/ spaces are handled correctly in loops below
IFS=

# for releases, add core hadoop jar to CLASSPATH
if [ -e $HADOOP_PREFIX/share/hadoop/hadoop-core-* ]; then
  for f in $HADOOP_PREFIX/share/hadoop/hadoop-core-*.jar; do
    CLASSPATH=${CLASSPATH}:$f;
  done

  # add libs to CLASSPATH
  for f in $HADOOP_PREFIX/share/hadoop/lib/*.jar; do
    CLASSPATH=${CLASSPATH}:$f;
  done
else
  # tarball layout
  if [ -e $HADOOP_HOME/hadoop-core-* ]; then
    for f in $HADOOP_HOME/hadoop-core-*.jar; do
      CLASSPATH=${CLASSPATH}:$f;
    done
  fi
  if [ -e $HADOOP_HOME/build/hadoop-core-* ]; then 
    for f in $HADOOP_HOME/build/hadoop-core-*.jar; do
      CLASSPATH=${CLASSPATH}:$f;
    done
  fi
  for f in $HADOOP_HOME/lib/*.jar; do
    CLASSPATH=${CLASSPATH}:$f;
  done

  if [ -d "$HADOOP_HOME/build/ivy/lib/Hadoop/common" ]; then
    for f in $HADOOP_HOME/build/ivy/lib/Hadoop/common/*.jar; do
      CLASSPATH=${CLASSPATH}:$f;
    done
  fi
fi

# Set the Vaidya home
if [ -d "$HADOOP_PREFIX/share/hadoop/contrib/vaidya/" ]; then
  VAIDYA_HOME=$HADOOP_PREFIX/share/hadoop/contrib/vaidya/
fi
if [ -d "$HADOOP_HOME/contrib/vaidya" ]; then
  VAIDYA_HOME=$HADOOP_HOME/contrib/vaidya/
fi
if [ -d "$HADOOP_HOME/build/contrib/vaidya" ]; then
  VAIDYA_HOME=$HADOOP_HOME/build/contrib/vaidya/
fi

# add user-specified CLASSPATH last
if [ "$HADOOP_USER_CLASSPATH_FIRST" = "" ] && [ "$HADOOP_CLASSPATH" != "" ]; then
  CLASSPATH=${CLASSPATH}:${HADOOP_CLASSPATH}
fi

# restore ordinary behaviour
unset IFS

$JAVA_HOME/bin/java -Xmx1024m -classpath $VAIDYA_HOME/hadoop-vaidya-${hadoopVersion}.jar:${CLASSPATH} org.apache.hadoop.vaidya.postexdiagnosis.PostExPerformanceDiagnoser $@
