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

                 ########### CHANGE THESE ONES!!!! ###########
# Uncomment these lines here and change the values to match your environment.
# JAVA_HOME=/usr/share/java
# JAVA_OPTS=""
# JBOSS_HOME=/usr/local/jboss
# OP_HOME=/usr/local/openportal
                 ########### CHANGE THESE ONES!!!! ###########

################################################################################
# settings which don't normally change below this point
CLASSPATH="$JBOSS_HOME/server/default/lib/hsqldb.jar"
DB_BASE="//localhost:1701:portal"
DB_URL="jdbc:hsqldb:hsql:"
################################################################################
# default values below this point: you really shouldn't need to change this
if [ "x$JAVA_HOME" = "x" ]; then
    JAVA_HOME=/usr/share/java
fi
if [ "x$JBOSS_HOME" = "x" ]; then
    JBOSS_HOME=/usr/local/jboss
fi
if [ "x$OP_HOME" = "x" ]; then
    OP_HOME=/usr/local/openportal
fi
if [ "x$OPHSQL_HOME" = "x" ]; then
    OPHSQL_HOME="$OP_HOME/db/hsql"
fi
################################################################################
# put out the settings so you can see what's happening when you run the scripts
echo
echo "HSQLDB is using the following settings:"
echo " ... CLASSPATH:   "$CLASSPATH
echo " ... JAVA_HOME:   "$JAVA_HOME
echo " ... JAVA_OPTS:   "$JAVA_OPTS
echo " ... JBOSS_HOME:  "$JBOSS_HOME
echo " ... OP_HOME:     "$OP_HOME
echo " ... OPHSQL_HOME: "$OPHSQL_HOME
echo
################################################################################

