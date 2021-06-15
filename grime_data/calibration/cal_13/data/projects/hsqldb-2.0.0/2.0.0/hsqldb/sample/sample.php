#!/usr/bin/php5
<?php
/*
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

# $Id: sample.php 3633 2010-06-06 23:56:41Z unsaved $

# Sample PHP script accessing HyperSQL through the ODBC extension module.
# (Therefore, you need to have the PHP ODBC module installed).

# This test HyperSQL client uses the ODBC DSN "tstdsn" to connect up to a
# HyperSQL server.  Just configure your own DSN to use the HyperSQL ODBC
# driver, specifying the HyperSQL server host name, database name, user,
# password, etc.

# Author:  Blaine Simpson  (blaine dot simpson at admc dot com)


# Empty strings for the username or password parameter here will defer
# to the ODBC manager for those values.  I.e. the blanks here do not mean to
# send blanks to the database server.
$conn_id = odbc_connect('tstdsn', '', '');
if (!$conn_id) exit('Connection Failed: ' . $conn_id . "\n");

if (!odbc_autocommit($conn_id, FALSE))
    exit("Failed to turn off AutoCommit mode\n");

if (!odbc_exec($conn_id, "DROP TABLE tsttbl IF EXISTS"))
    exit("DROP command failed\n");

if (!odbc_exec($conn_id,
"CREATE TABLE tsttbl(
    id BIGINT generated BY DEFAULT AS IDENTITY,
    vc VARCHAR(20),
    entrytime TIMESTAMP DEFAULT current_timestamp NOT NULL
)"))
    exit("CREATE TABLE command failed\n");


# First do a non-parameterized insert
if (!odbc_exec($conn_id, "INSERT INTO tsttbl(id, vc) VALUES(1, 'one')"))
    exit("Insertion of first row failed\n");

# Now parameterized inserts
$stmt = odbc_prepare($conn_id, "INSERT INTO tsttbl(id, vc) VALUES(?, ?)");
if (!$stmt) exit("Preparation of INSERT statement failed \n");

# With (default) debug mode, the following statements will generate
# annoying "cursor updatability" warnings.
$rv = odbc_execute($stmt, array(2, 'two'));
if ($rv != 1) exit("2nd Insertion failed with  value $rv\n");
$rv = odbc_execute($stmt, array(3, 'three'));
if ($rv != 1) exit("3rd Insertion failed with  value $rv\n");
$rv = odbc_execute($stmt, array(4, 'four'));
if ($rv != 1) exit("4th Insertion failed with  value $rv\n");
$rv = odbc_execute($stmt, array(5, 'five'));
if ($rv != 1) exit("5th Insertion failed with  value $rv\n");
odbc_commit($conn_id);

# A non-parameterized query
$rs = odbc_exec($conn_id, "SELECT * FROM tsttbl WHERE id < 3");
if (!$rs) exit("Error in SQL\n");

$rownum = 0;
while (odbc_fetch_row($rs)) {
    $rownum++;
    echo "$rownum: " .  odbc_result($rs, "id")
        . '|' . odbc_result($rs, "vc")
        . '|' . odbc_result($rs, "entrytime") . "\n";
}

# You need to use the PDO_ODBC extension to parameterize queries (selects).
# If you want an example of this, just ask.

odbc_close($conn_id);

?>
