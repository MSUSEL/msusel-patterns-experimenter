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
 * $Id: dsv-constcols.sql 610 2008-12-22 15:54:18Z unsaved $
 *
 * Tests setting column values with *DSV_CONST_COLS
 */

* *DSV_CONST_COLS= d = 2007-05-14 0:00:00  |   a  =  139  

CREATE TABLE t (i INT, a INT, d DATE);

\m dsv-constcols.dsv
SELECT COUNT(*) FROM t WHERE a = 139 AND d = '2007-05-14';

/* The d const value will override the .dsv-specified values. */
*if (*? != 2)
    \q Import using constants for int and date columns failed
*end if

DELETE from t;
* *DSV_CONST_COLS=
\m dsv-constcols.dsv

SELECT COUNT(*) FROM t WHERE a IS null AND d IS null;
*if (*? != 1)
    \q Failed to reset CONST_COLS behavior (1)
*end if

SELECT COUNT(*) FROM t WHERE a IS null AND d = '2006-12-11';
*if (*? != 1)
    \q Failed to reset CONST_COLS behavior (2)
*end if
