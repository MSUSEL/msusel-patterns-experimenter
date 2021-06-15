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
 * $Id: dsv-sqlarray.sql 3603 2010-06-01 02:07:46Z unsaved $
 *
 * Tests a DSV "round trip".
 * Create a table, export the data, import it back, cf. imported  and original.
 */

* *DSV_TARGET_FILE = ${java.io.tmpdir}/test-sqlarray-${user.name}.dsv
* *DSV_TARGET_TABLE = t

CREATE TABLE t (i BIGINT PRIMARY KEY, ar INTEGER ARRAY);

INSERT INTO t VALUES (1, array [11, null, 13]);
INSERT INTO t VALUES (2, null);
INSERT INTO t VALUES (3, array [21, 22]);
COMMIT;

/* Export */
\x t

SELECT count(*) FROM t WHERE ar IS NULL AND i = 2;
*if (*? != 1)
    \q Pre-check of inserted Array array data failed (1)
*end if
SELECT count(*) FROM t WHERE i in (1, 3) AND ar IS NOT null;
*if (*? != 2)
    \q Pre-check of inserted Array data failed (2)
*end if

DELETE FROM t;

SELECT count(*) FROM t;
*if (*? != 0)
    \q Failed to clear table data
*end if

/* Import */
\m *{*DSV_TARGET_FILE}
SELECT count(*) FROM t WHERE ar IS NULL AND i = 2;
*if (*? != 1)
    \q Post-check of inserted Array array data failed (1)
*end if
SELECT count(*) FROM t WHERE i in (1, 3) AND ar IS NOT null;
*if (*? != 2)
    \q Post-check of inserted Array data failed (2)
*end if
