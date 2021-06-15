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


echo
echo "James Build System"
echo "-------------------"

export ANT_HOME=$ANT_HOME
ANT_HOME=./tools

export OLD_CLASSPATH=$CLASSPATH
CLASSPATH=phoenix-bin/lib/xercesImpl-2.0.2.jar:phoenix-bin/lib/xml-apis.jar

## Setup the Anakia stuff
if [ -d ../jakarta-site2/lib ] ; then
for i in ../jakarta-site2/lib/velocity*.jar
do
    CLASSPATH=${CLASSPATH}:$i
done
for i in ../jakarta-site2/lib/jdom*.jar
do
    CLASSPATH=${CLASSPATH}:$i
done
for i in ../jakarta-site2/lib/xerces*.jar
do
    CLASSPATH=${CLASSPATH}:$i
done
echo "Jakarta-Site2 Module Found"
fi

export CLASSPATH

chmod u+x ${ANT_HOME}/bin/antRun
chmod u+x ${ANT_HOME}/bin/ant

export PROPOSAL=""


${ANT_HOME}/bin/ant -emacs $@

export CLASSPATH=$OLD_CLASSPATH
export ANT_HOME=$OLD_ANT_HOME
