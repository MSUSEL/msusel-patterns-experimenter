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
 * $Id: lastval.sql 610 2008-12-22 15:54:18Z unsaved $
 *
 * Tests auto-variable ?
 */

CREATE TABLE t (i INT);

* if (*? != 0)
    \q ? variable not capturing CREATE TABLE return value
* end if

INSERT INTO t values (21);
* if (*? != 1)
    \q ? variable not capturing INSERT return value
* end if

INSERT INTO t values (10);
* if (*? != 1)
    \q ? variable not capturing INSERT return value
* end if

INSERT INTO t values (43);
* if (*? != 1)
    \q ? variable not capturing INSERT return value
* end if

SELECT * FROM t ORDER BY i DESC;
* if (*? != 10)
    \q ? variable not capturing last fetched value
* end if

\p echo some stuff
\p to verify that ? variable value is preserved
* list

* if (*? != 10)
    \q ? value not retained after special commands
* end if
* if (*{?} != 10)
    \q ? value not dereferenced with {} usage
* end if
