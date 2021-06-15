--
-- The MIT License (MIT)
--
-- MSUSEL Arc Framework
-- Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
-- Software Engineering Laboratory and Idaho State University, Informatics and
-- Computer Science, Empirical Software Engineering Laboratory
--
-- Permission is hereby granted, free of charge, to any person obtaining a copy
-- of this software and associated documentation files (the "Software"), to deal
-- in the Software without restriction, including without limitation the rights
-- to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
-- copies of the Software, and to permit persons to whom the Software is
-- furnished to do so, subject to the following conditions:
--
-- The above copyright notice and this permission notice shall be included in all
-- copies or substantial portions of the Software.
--
-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
-- IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
-- FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
-- AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
-- LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
-- OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
-- SOFTWARE.
--

/*
    $Id: j-sample.sql 3605 2010-06-01 02:21:36Z unsaved $
    Exemplifies use of SqlTool's \j command to specify the JDBC connection
    parameters right in the SQL file.

    Invoke like this:

        java -jar .../sqltool.jar - .../j-sample.sql

    (give the file paths to wherever these two files reside).
    Or start up SqlTool like this:

        java -jar .../sqltool.jar

    and then execute this script like

        \i .../j-sample.sql
*/

-- Abort this script when errors occur.
-- That's the default if the script is invoked from command-line, but not if
-- invoked by \i.
\c false
 
-- Note the new feature in HyperSQL 2, whereby you can set an SA password
-- by just specifying that as the password for the very first connection to
-- that database
\j SA fred jdbc:hsqldb:mem:fred
--  FORMAT:  \j <USERAME> <PASSWORD> <JDBC_URL>

\p You have conkected successfully
\p

CREATE TABLE t(i BIGINT, vc VARCHAR(20));
INSERT INTO t VALUES(1, 'one');
INSERT INTO t VALUES(2, 'two');

SELECT * FROM t;
