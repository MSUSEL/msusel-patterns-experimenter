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

#
# Note: This script depends on 5 environment variables to function correctly:
# a) CLASSPATH
# b) HADOOP_HOME
# c) HADOOP_CONF_DIR 
# d) HADOOP_LOG_DIR 
# e) LIBHDFS_BUILD_DIR
# f) LIBHDFS_INSTALL_DIR
# g) OS_NAME
# All these are passed by build.xml.
#

HDFS_TEST=hdfs_test
HADOOP_LIB_DIR=$HADOOP_HOME/lib
HADOOP_BIN_DIR=$HADOOP_HOME/bin

# Manipulate HADOOP_CONF_DIR too
# which is necessary to circumvent bin/hadoop
HADOOP_CONF_DIR=$HADOOP_CONF_DIR:$HADOOP_HOME/conf

# set pid file dir so they are not written to /tmp
export HADOOP_PID_DIR=$HADOOP_LOG_DIR

# CLASSPATH initially contains $HADOOP_CONF_DIR
CLASSPATH="${HADOOP_CONF_DIR}"
CLASSPATH=${CLASSPATH}:$JAVA_HOME/lib/tools.jar

# for developers, add Hadoop classes to CLASSPATH
if [ -d "$HADOOP_HOME/build/classes" ]; then
  CLASSPATH=${CLASSPATH}:$HADOOP_HOME/build/classes
fi
if [ -d "$HADOOP_HOME/build/webapps" ]; then
  CLASSPATH=${CLASSPATH}:$HADOOP_HOME/build
fi
if [ -d "$HADOOP_HOME/build/test/classes" ]; then
  CLASSPATH=${CLASSPATH}:$HADOOP_HOME/build/test/classes
fi

# so that filenames w/ spaces are handled correctly in loops below
IFS=

# add libs to CLASSPATH
for f in $HADOOP_HOME/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done

for ff in $HADOOP_HOME/*.jar; do 
  CLASSPATH=${CLASSPATH}:$ff
done
for f in $HADOOP_HOME/lib/jsp-2.0/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done

if [ -d "$HADOOP_HOME/build/ivy/lib/Hadoop/common" ]; then
for f in $HADOOP_HOME/build/ivy/lib/Hadoop/common/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done
fi

# restore ordinary behaviour
unset IFS

findlibjvm () {
javabasedir=$JAVA_HOME
case $OS_NAME in
    cygwin* | mingw* | pw23* )
    lib_jvm_dir=`find $javabasedir -follow \( \
        \( -name client -type d -prune \) -o \
        \( -name "jvm.dll" -exec dirname {} \; \) \) 2> /dev/null | tr "\n" " "`
    ;;
    aix*)
    lib_jvm_dir=`find $javabasedir \( \
        \( -name client -type d -prune \) -o \
        \( -name "libjvm.*" -exec dirname {} \; \) \) 2> /dev/null | tr "\n" " "`
    if test -z "$lib_jvm_dir"; then
       lib_jvm_dir=`find $javabasedir \( \
       \( -name client -type d -prune \) -o \
       \( -name "libkaffevm.*" -exec dirname {} \; \) \) 2> /dev/null | tr "\n" " "`
    fi
    ;;
    *)
    lib_jvm_dir=`find $javabasedir -follow \( \
       \( -name client -type d -prune \) -o \
       \( -name "libjvm.*" -exec dirname {} \; \) \) 2> /dev/null | tr "\n" " "`
    if test -z "$lib_jvm_dir"; then
       lib_jvm_dir=`find $javabasedir -follow \( \
       \( -name client -type d -prune \) -o \
       \( -name "libkaffevm.*" -exec dirname {} \; \) \) 2> /dev/null | tr "\n" " "`
    fi
    ;;
  esac
  echo $lib_jvm_dir
}
LIB_JVM_DIR=`findlibjvm`
echo  "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
echo  LIB_JVM_DIR = $LIB_JVM_DIR
echo  "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
# Put delays to ensure hdfs is up and running and also shuts down 
# after the tests are complete
cd $HADOOP_HOME
echo Y | $HADOOP_BIN_DIR/hadoop namenode -format &&
$HADOOP_BIN_DIR/hadoop-daemon.sh start namenode && sleep 2 && 
$HADOOP_BIN_DIR/hadoop-daemon.sh start datanode && sleep 2 && 
sleep 20
echo CLASSPATH=$HADOOP_CONF_DIR:$CLASSPATH LD_PRELOAD="$LIBHDFS_INSTALL_DIR/libhdfs.so:$LIB_JVM_DIR/libjvm.so" $LIBHDFS_BUILD_DIR/$HDFS_TEST && 
CLASSPATH=$HADOOP_CONF_DIR:$CLASSPATH LD_PRELOAD="$LIB_JVM_DIR/libjvm.so:$LIBHDFS_INSTALL_DIR/libhdfs.so:" $LIBHDFS_BUILD_DIR/$HDFS_TEST
BUILD_STATUS=$?
sleep 3
$HADOOP_BIN_DIR/hadoop-daemon.sh stop datanode && sleep 2 && 
$HADOOP_BIN_DIR/hadoop-daemon.sh stop namenode && sleep 2 

echo exiting with $BUILD_STATUS
exit $BUILD_STATUS
