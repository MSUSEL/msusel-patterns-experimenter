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
 * $Id: dsv-omits.sql$
 *
 * Tests omitting columns via header line and *DSV_SKIP_COLS
 */

CREATE TABLE t (i INT, a INT, b INT, c INT);

\m dsv-omits.dsv
SELECT COUNT(*) FROM t
WHERE i IS NOT null AND a IS NOT null AND b IS NOT null AND c IS NOT null;

*if (*? != 2)
    \q Import using header line - column-skips failed
*end if

/** Repeat test with some non-default DSV settings */
* *DSV_SKIP_COLS =   c|   a 

DELETE FROM t;
\m dsv-omits.dsv

SELECT COUNT(*) FROM t
WHERE i IS NOT null AND b IS NOT null AND a IS null AND c IS null;

*if (*? != 2)
    \q Import using header line - AND *DSV_SKIP_COLS column-skips failed
*end if

/* Now test that behavior reverts when PL variable is cleared */
* *DSV_SKIP_COLS =
* listvalues

DELETE FROM t;
\m dsv-omits.dsv
SELECT COUNT(*) FROM t
WHERE i IS NOT null AND a IS NOT null AND b IS NOT null AND c IS NOT null;
\p Post everything

*if (*? != 2)
    SELECT * FROM t;
    \q *DSV_SKIP_COLS behavior failed to revert when variable was cleared
*end if
