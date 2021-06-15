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
 * $Id: dsv-rpcommit.sql 437 2008-11-14 04:17:35Z unsaved $
 *
 * Tests *DSV_RECORDS_PER_COMMIT setting
 */

/* Unfortunately, can't test how many commits are performed and where.
 * That would * require adding significant complexity to SqlFile itself, or
 * complex interception of commits.
 * I have, however, manually tested the commits by echoing the commits in
 * the source code, and removing observing what remains uncommitted after
 * removing the straggler-catching commit after the insertion loop in
 * SqlFile.importDsv().
 * All that we are testing here is that all records which should be committed
 * do get committed.
 */

CREATE TABLE t (i INT);

\c true
* *DSV_RECORDS_PER_COMMIT=5
\m dsv-rpcommit.dsv

ROLLBACK;

SELECT COUNT(*) FROM t;
*if (*? != 13)
    \q *DSV_RECORDS_PER_COMMIT committed only *{?} records instead 13
*end if

DELETE FROM t;
* *DSV_REJECT_REPORT = /dev/null
\m dsv-rpcommit.dsv

SELECT COUNT(*) FROM t;
*if (*? != 21)
    \q *DSV_RECORDS_PER_COMMIT committed only *{?} records instead 21
*end if
