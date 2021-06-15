#!/usr/bin/python
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


# $Id: sample.py 3633 2010-06-06 23:56:41Z unsaved $

# Sample Python script accessing HyperSQL through the Python pyodbc module.

# This test HyperSQL client uses the ODBC DSN "tstdsn-a" to connect up to a
# HyperSQL server.  Just configure your own DSN to use the HyperSQL ODBC
# driver, specifying the HyperSQL server host name, database name, user,
# password, etc.

# N.b. there is some dependency or bug which requires pyodbc to use the
# ANSI variant of the HyperSQL ODBC Driver.  Using the normal Unicode
# variant will generate the following error message when you try to connect:
#    pyodbc.Error: ('0', '[0] [unixODBC]c (202) (SQLDriverConnectW)')
# It is quite possible that this issue will be taken care of when we fix a
# high-priority bug to do with switching between SQLDriverConnect and
# SQLDriverConnectW on UNIX.

# Author:  Blaine Simpson  (blaine dot simpson at admc dot com)

import pyodbc

# Get a connection handle.
# In addition to the DSN name, you can override or supply additional DSN
# settings, such as "Uid" and "Pwd"; or define the DSN from scratch, starting
# with Driver.  These settings are delimited with "; ".  See pyodbc docs.
conn = pyodbc.connect("DSN=tstdsn-a")
try:
    conn.autocommit = 0

    cursor = conn.cursor();

    cursor.execute("DROP TABLE tsttbl IF EXISTS");
    conn.commit();  # Some recent change to the HyperSQL server or to unixODBC
                    # has made this necessary, at least on UNIX.  Some other
                    # transaction control command would probably be more
                    # appropriate here.

    cursor.execute(
        "CREATE TABLE tsttbl(\n"
        + "    id BIGINT generated BY DEFAULT AS IDENTITY,\n"
        + "    vc VARCHAR(20),\n"
        + "    entrytime TIMESTAMP DEFAULT current_timestamp NOT NULL\n"
        + ")");

    # First a simple/non-parameterized Insertion
    retval = cursor.execute("INSERT INTO tsttbl (id, vc) values (1, 'one')");
    if retval != 1:
        raise Exception(('1st insertion inserted ' + repr(retval)
            + ' rows instead of 1'))
    # Now parameterized.  Unfortunately, the Python DB API and pyodbc API do
    # not allow re-use of a parsed statement.  Cursor must be reparsed for
    # each usage.
    retval = cursor.execute("INSERT INTO tsttbl (id, vc) values (?, ?)",
            2, 'two');
    if retval != 1:
        raise Exception(('2nd insertion inserted ' + repr(retval)
            + ' rows instead of 2'))
    retval = cursor.execute("INSERT INTO tsttbl (id, vc) values (?, ?)",
            3, 'three');
    if retval != 1:
        raise Exception(('3rd insertion inserted ' + repr(retval)
            + ' rows instead of 3'))
    retval = cursor.execute("INSERT INTO tsttbl (id, vc) values (?, ?)",
            4, 'four');
    if retval != 1:
        raise Exception(('4th insertion inserted ' + repr(retval)
            + ' rows instead of 4'))
    retval = cursor.execute("INSERT INTO tsttbl (id, vc) values (?, ?)",
            5, 'five');
    if retval != 1:
        raise Exception(('5th insertion inserted ' + repr(retval)
            + ' rows instead of 5'))
    conn.commit();

    # Non-parameterized query
    for row in cursor.execute(
            "SELECT * FROM tsttbl WHERE id < 3"):
        print row

    # Non-parameterized query.  As noted above, can't re-use parsed cursor.
    for row in cursor.execute(
            "SELECT * FROM tsttbl WHERE id > ?", 3):
        # For variety, we format the files ourselves this time
        print repr(row.ID) + '|' + row.VC + '|' + repr(row.ENTRYTIME)

except Exception as e:
    conn.rollback();
    raise e

finally:
    conn.close();
