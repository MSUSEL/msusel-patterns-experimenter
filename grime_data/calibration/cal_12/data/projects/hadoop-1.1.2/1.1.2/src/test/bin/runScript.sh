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


TZ=PDT8PST  date
TZ=EDT5EST  date
env|grep -i branch

if [ $# -eq 0 ]
then
	echo "No arguments given. Exiting." 1>&2
	exit 1
fi
	if [ -z "$GIT_REPO" -a -z "$SVN_REPO" ]
	then
		echo "Serious error: neither SVN_REPO nor GIT_REPO were specified." 1>&2
		exit 1
	fi
	if [ -n "$GIT_REPO" -a -n "$SVN_REPO" ]
	then
		echo "Serious error: both SVN_REPO and GIT_REPO were specified." 1>&2
		exit 1
	fi
	if [ -n "$GIT_REPO" ]
	then
		if [ -d hudson ]
		then
	    		(cd hudson && git pull)
		else
			[ -d internal ] && rm -rf internal
	    		git clone $GIT_REPO internal
			if [ -n "$GIT_SCRIPTS_BRANCH" ]
			then
				(cd internal && git checkout $GIT_SCRIPTS_BRANCH)
			fi
		fi
	fi
	if [ -n "$SVN_REPO" ]
	then
		svn co $SVN_REPO
	fi

echo  ============  hostname = `hostname`

echo "This script does not yet unset JAVA_CMD, LD_LIBRARY_PATH, and does not yet set JAVA_HOME." | fmt
set -x
exec $*
