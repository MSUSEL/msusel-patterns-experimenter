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


# main script invoked by hudson

# sanity check parameters
[ -z $BRANCH ] && echo "BRANCH variable mandatory" && exit 1
[ -z $VERSION ] && echo "VERSION variable mandatory" && exit 1
[ -z $GIT_USER ] && echo "GIT_USER variable mandatory" && exit 1
[ -z $GIT_EMAIL ] && echo "GIT_EMAIL variable mandatory" && exit 1

OPTS="-b $BRANCH"
if [ ! -z $REV ]; then
  OPTS="$OPTS -r $REV"
fi

if [ ! -z $JAVA_HOME ]; then
  export PATH=$JAVA_HOME/bin:$PATH
fi
./build_release.sh $OPTS $VERSION $GIT_USER $GIT_EMAIL
