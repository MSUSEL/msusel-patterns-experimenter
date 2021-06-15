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

COMMANDS=( "qstat" "qalter" "checkjob" )
ERROR=0
for (( i=0; i<${#COMMANDS[@]}; i++ ))
do
  cmd=${COMMANDS[$i]}
  CMD_PATH=`which $cmd 2>/dev/null`
  if [ $? -ne 0 ]
  then
    echo Could not find $cmd in PATH
    ERROR=1
  fi
done
if [ $ERROR -ne 0 ]
then
  exit 1
fi

jobs=`qstat -i |grep -o -e '^[0-9]*'`
for job in $jobs
do
  echo -en "$job\t"
  PATTERN="job [^ ]* violates active HARD MAXPROC limit of \([0-9]*\) for user [^ ]*[ ]*(R: \([0-9]*\), U: \([0-9]*\))"
  OUT=`checkjob $job 2>&1|grep -o -e "$PATTERN"`
  if [ $? -eq 0 ]
  then
    echo -en "| Exceeds resource limits\t"
    COMMENT_FIELD=`echo $OUT|sed -e "s/$PATTERN/User-limits exceeded. Requested:\2 Used:\3 MaxLimit:\1/"`
    qstat -f $job|grep '^[ \t]*comment = .*$' >/dev/null
    if [ $? -ne 0 ]
    then
      echo -en "| Comment field updated\t"
      qalter $job -W comment="$COMMENT_FIELD" >/dev/null
    else
      echo -en "| Comment field already set\t"
    fi
  else
    echo -en "| Doesn't exceed limits.\t"
  fi
  echo
done
