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


GP_HOME=.
COMMAND_PATH=`echo ${0} | sed -e "s/\(.*\)\/.*$/\1/g"`
cd ${COMMAND_PATH}

LOCAL_CLASSPATH=$GP_HOME/eclipsito.jar

CONFIGURATION_FILE=ganttproject-eclipsito-config.xml
BOOT_CLASS=org.bardsoftware.eclipsito.Boot
LOG_FILE=$HOME/.ganttproject.log

if [ -z $JAVA_HOME ]; then
  JAVA_COMMAND=`which java`
  if [ "$?" = "1" ]; then
    echo "No executable java found. Please set JAVA_HOME variable";
    exit;
  fi
else
  JAVA_COMMAND=$JAVA_HOME/bin/java
fi
if [ ! -x $JAVA_COMMAND ]; then
  echo "$JAVA_COMMAND is not executable. Please check the permissions."
  exit
fi

if [ -e $LOG_FILE ] && [ ! -w $LOG_FILE ]; then
  echo "Log file $LOG_FILE is not writable"
  exit
fi

$JAVA_COMMAND -Xmx256m -classpath $CLASSPATH:$LOCAL_CLASSPATH $BOOT_CLASS $CONFIGURATION_FILE -log $LOG_FILE "$@" 


