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

this="${BASH_SOURCE-$0}"
bin=$(cd -P -- "$(dirname -- "$this")" && pwd -P)
script="$(basename -- "$this")"
this="$bin/$script"

if [ "$HADOOP_HOME" != "" ]; then
  echo "Warning: \$HADOOP_HOME is deprecated."
  echo
fi

. "$bin"/../libexec/hadoop-config.sh

usage() {
  echo "
usage: $0 <parameters>
  Require parameter:
     --config /etc/hadoop                                  Location of Hadoop configuration file
     -u <username>                                         Create user on HDFS
  Optional parameters:
     -h                                                    Display this message
     --kerberos-realm=KERBEROS.EXAMPLE.COM                 Set Kerberos realm
     --super-user=hdfs                                     Set super user id
     --super-user-keytab=/etc/security/keytabs/hdfs.keytab Set super user keytab location
  "
  exit 1
}

OPTS=$(getopt \
  -n $0 \
  -o '' \
  -l 'kerberos-realm:' \
  -l 'super-user:' \
  -l 'super-user-keytab:' \
  -o 'h' \
  -o 'u' \
  -- "$@")

if [ $? != 0 ] ; then
    usage
    exit 1
fi

create_user() {
  if [ "${SETUP_USER}" = "" ]; then
    break
  fi
  HADOOP_HDFS_USER=${HADOOP_HDFS_USER:-hdfs}
  export HADOOP_PREFIX
  export HADOOP_CONF_DIR
  export JAVA_HOME
  export SETUP_USER=${SETUP_USER}
  export SETUP_PATH=/user/${SETUP_USER}

  if [ ! "${KERBEROS_REALM}" = "" ]; then
    # locate kinit cmd
    if [ -e /etc/lsb-release ]; then
      KINIT_CMD="/usr/bin/kinit -kt ${HDFS_USER_KEYTAB} ${HADOOP_HDFS_USER}"
    else
      KINIT_CMD="/usr/kerberos/bin/kinit -kt ${HDFS_USER_KEYTAB} ${HADOOP_HDFS_USER}"
    fi
    su -c "${KINIT_CMD}" ${HADOOP_HDFS_USER}
  fi

  su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} fs -mkdir ${SETUP_PATH}" ${HADOOP_HDFS_USER}
  su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} fs -chown ${SETUP_USER}:${SETUP_USER} ${SETUP_PATH}" ${HADOOP_HDFS_USER}
  su -c "${HADOOP_PREFIX}/bin/hadoop --config ${HADOOP_CONF_DIR} fs -chmod 711 ${SETUP_PATH}" ${HADOOP_HDFS_USER}

  if [ "$?" == "0" ]; then
    echo "User directory has been setup: ${SETUP_PATH}"
  fi
}

eval set -- "${OPTS}"
while true; do
  case "$1" in
    -u)
      shift
      ;;
    --kerberos-realm)
      KERBEROS_REALM=$2; shift 2
      ;;
    --super-user)
      HADOOP_HDFS_USER=$2; shift 2
      ;;
    --super-user-keytab)
      HDFS_USER_KEYTAB=$2; shift 2
      ;;
    -h)
      usage
      ;; 
    --)
      while shift; do
        SETUP_USER=$1
        create_user
      done
      break
      ;;
    *)
      echo "Unknown option: $1"
      usage
      exit 1 
      ;;
  esac
done 

