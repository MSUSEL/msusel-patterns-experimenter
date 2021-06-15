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

# location of useful commands
CREATEDB="/usr/bin/createdb"
DROPDB="/usr/bin/dropdb"
PSQL="/usr/bin/psql -e"
SLEEP="/bin/sleep"
DB_NAME=ivatagroupware

# first drop and recreate the portal db
$DROPDB $DB_NAME
$SLEEP 1
$CREATEDB $DB_NAME || exit -1

echo "################################################################################"
echo "FIRST INITIALIZE TO 0.9 STATE"
echo "################################################################################"
if test -e ../../../db/psql/upgrade/local.sql; then
    $PSQL $DB_NAME < ../../../db/psql/upgrade/local.sql || exit -1
else
    $PSQL $DB_NAME < ../../../db/psql/upgrade/state-0.9.sql || exit -1
fi

echo "################################################################################"
echo "UPDATES SINCE 0.9 BEING APPLIED NEXT"
echo "################################################################################"
$PSQL $DB_NAME < ../../../db/psql/upgrade/upgrade-0.10.sql || exit -1
echo "################################################################################"


