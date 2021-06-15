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
 * $Id: dsv-trimming.sql 3340 2009-12-14 00:00:49Z unsaved $
 *
 * Tests trimming in DSV imports
 */

CREATE TABLE t (i INT, r REAL, d DATE, t TIMESTAMP, v VARCHAR(80), b BOOLEAN);

\m dsv-trimming.dsv

SELECT count(*)  FROM t WHERE i = 31;
*if (*? != 1)
    \q Import of space-embedded INT failed
*end if

SELECT count(*)  FROM t WHERE r = 3.124;
*if (*? != 1)
    \q Import of space-embedded REAL failed
*end if

SELECT count(*)  FROM t WHERE d = '2007-06-07';
*if (*? != 1)
    \q Import of space-embedded DATE failed (1)
*end if

SELECT count(*)  FROM t WHERE t = '2006-05-06 12:30:04';
*if (*? != 1)
    \q Import of space-embedded TIMESTAMP failed
*end if

SELECT count(*)  FROM t WHERE v = '  a B  ';
*if (*? != 1)
    \q Import of space-embedded VARCHAR failed
*end if

/** I dont' know if "IS true" or "= true" is preferred, but the former
 * doesn't work with HSQLDB 1.7.0.7 */
SELECT count(*)  FROM t WHERE b = true;
*if (*? != 1)
    \q Import of space-embedded BOOLEAN failed
*end if


/** Repeat test with some non-default DSV settings */
* *DSV_COL_SPLITTER = \\
* *DSV_ROW_SPLITTER = \}(?:\r\n|\r|\n)

DELETE FROM t;

\m dsv-trimming-alt.dsv

SELECT count(*)  FROM t WHERE i = 31;
*if (*? != 1)
    \q Import of space-embedded INT failed
*end if

SELECT count(*)  FROM t WHERE r = 3.124;
*if (*? != 1)
    \q Import of space-embedded REAL failed
*end if

SELECT count(*)  FROM t WHERE d = '2007-06-07';
*if (*? != 1)
    \q Import of space-embedded DATE failed (2)
*end if

SELECT count(*)  FROM t WHERE t = '2006-05-06 12:30:04';
*if (*? != 1)
    \q Import of space-embedded TIMESTAMP failed
*end if

SELECT count(*)  FROM t WHERE v = '  a B  ';
*if (*? != 1)
    \q Import of space-embedded VARCHAR failed
*end if

/** I dont' know if "IS true" or "= true" is preferred, but the former
 * doesn't work with HSQLDB 1.7.0.7 */
SELECT count(*)  FROM t WHERE b = true;
*if (*? != 1)
    \q Import of space-embedded BOOLEAN failed
*end if
