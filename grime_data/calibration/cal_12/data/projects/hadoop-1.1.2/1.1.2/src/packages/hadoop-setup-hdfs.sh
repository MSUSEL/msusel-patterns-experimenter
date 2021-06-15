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

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

. "$bin"/../libexec/hadoop-config.sh

usage() {
  echo "
usage: $0 <parameters>

  Optional parameters:
     --format                                                        Force namenode format
     --group=hadoop                                                  Set Hadoop group
     -h                                                              Display this message
     --hdfs-user=hdfs                                                Set HDFS user
     --kerberos-realm=KERBEROS.EXAMPLE.COM                           Set Kerberos realm
     --hdfs-user-keytab=/home/hdfs/hdfs.keytab                       Set HDFS user key tab
     --mapreduce-user=mr                                             Set mapreduce user
  "
  exit 1
}

OPTS=$(getopt \
  -n $0 \
  -o '' \
  -l 'format' \
  -l 'hdfs-user:' \
  -l 'hdfs-user-keytab:' \
  -l 'mapreduce-user:' \
  -l 'kerberos-realm:' \
  -o 'h' \
  -- "$@")

if [ $? != 0 ] ; then
    usage
fi

eval set -- "${OPTS}"
while true ; do
  case "$1" in
    --format)
      FORMAT_NAMENODE=1; shift
      AUTOMATED=1
      ;;
    --group)
      HADOOP_GROUP=$2; shift 2
      AUTOMATED=1
      ;;
    --hdfs-user)
      HADOOP_HDFS_USER=$2; shift 2
      AUTOMATED=1
      ;;
    --mapreduce-user)
      HADOOP_MR_USER=$2; shift 2
      AUTOMATED=1
      ;;
    --hdfs-user-keytab)
      HDFS_KEYTAB=$2; shift 2
      AUTOMATED=1
      ;;
    --kerberos-realm)
      KERBEROS_REALM=$2; shift 2
      AUTOMATED=1
      ;;
    --)
      shift ; break
      ;;
    *)
      echo "Unknown option: $1"
      usage
      exit 1
      ;;
  esac
done

HADOOP_GROUP=${HADOOP_GROUP:-hadoop}
HADOOP_HDFS_USER=${HADOOP_HDFS_USER:-hdfs}
HADOOP_MAPREDUCE_USER=${HADOOP_MR_USER:-mapred}

if [ "${KERBEROS_REALM}" != "" ]; then
  # Determine kerberos location base on Linux distro.
  if [ -e /etc/lsb-release ]; then
    KERBEROS_BIN=/usr/bin
  else
    KERBEROS_BIN=/usr/kerberos/bin
  fi
  kinit_cmd="${KERBEROS_BIN}/kinit -k -t ${HDFS_KEYTAB} ${HADOOP_HDFS_USER}"
  su -c "${kinit_cmd}" ${HADOOP_HDFS_USER}
fi

echo "Setup Hadoop Distributed File System"
echo

# Format namenode
if [ "${FORMAT_NAMENODE}" == "1" ]; then
  echo "Formatting namenode"
  echo
  su -c "echo Y | ${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} namenode -format" ${HADOOP_HDFS_USER}
  echo
fi

# Start namenode process
echo "Starting namenode process"
echo
if [ -e ${HADOOP_PREFIX}/sbin/hadoop-daemon.sh ]; then
  DAEMON_PATH=${HADOOP_PREFIX}/sbin
else
  DAEMON_PATH=${HADOOP_PREFIX}/bin
fi
su -c "${DAEMON_PATH}/hadoop-daemon.sh --config ${HADOOP_CONF_DIR} start namenode" ${HADOOP_HDFS_USER}
echo
echo "Initialize HDFS file system: "
echo

#create the /user dir 
su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} dfs -mkdir /user" ${HADOOP_HDFS_USER}
su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} dfs -chmod 755 /user" ${HADOOP_HDFS_USER}

#create /tmp and give it 777
su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} dfs -mkdir /tmp" ${HADOOP_HDFS_USER}
su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} dfs -chmod 777 /tmp" ${HADOOP_HDFS_USER}

#create /mapred
su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} dfs -mkdir /mapred" ${HADOOP_HDFS_USER}
su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} dfs -chmod 755 /mapred" ${HADOOP_HDFS_USER}
su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} dfs -chown ${HADOOP_MAPREDUCE_USER}:${HADOOP_GROUP} /mapred" ${HADOOP_HDFS_USER}

if [ $? -eq 0 ]; then
  echo "Completed."
else
  echo "Unknown error occurred, check hadoop logs for details."
fi

echo
echo "Please startup datanode processes: /etc/init.d/hadoop-datanode start"
