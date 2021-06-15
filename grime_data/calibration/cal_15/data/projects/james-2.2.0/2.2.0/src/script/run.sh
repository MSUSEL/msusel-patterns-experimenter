#! /bin/sh
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
# Phoenix start script.
#
# Author: Peter Donald <donaldp@apache.org>
#
# The user may choose to supply parameters to the JVM (such as memory settings)
# via setting the environment variable PHOENIX_JVM_OPTS
#

# Checking for JAVA_HOME is required on *nix due
# to some distributions stupidly including kaffe in /usr/bin
if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

#
# Locate where phoenix is in filesystem
#
THIS_PROG=`dirname $0`

if [ "$THIS_PROG" = "." ] ; then
  THIS_PROG=$PWD
fi

PHOENIX_HOME=$THIS_PROG/..
unset THIS_PROG

# echo "Home directory: $PHOENIX_HOME"
# echo "Home ext directory: $PHOENIX_HOME/lib"

#
# Command to overide JVM ext dir
#
# This is needed as some JVM vendors do foolish things
# like placing jaxp/jaas/xml-parser jars in ext dir
# thus breaking Phoenix
#
JVM_OPTS="-Djava.ext.dirs=$PHOENIX_HOME/lib $PHOENIX_JVM_OPTS"

# Kicking the tires and lighting the fires!!!
$JAVA_HOME/bin/java $JVM_OPTS -jar $PHOENIX_HOME/bin/phoenix-loader.jar $*
