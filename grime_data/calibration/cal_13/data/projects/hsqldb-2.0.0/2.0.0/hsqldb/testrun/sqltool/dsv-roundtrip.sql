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
 * $Id: dsv-roundtrip.sql 610 2008-12-22 15:54:18Z unsaved $
 *
 * Tests a DSV "round trip".
 * Create a table, export the data, import it back, cf. imported  and original.
 */

* *DSV_TARGET_FILE = ${java.io.tmpdir}/test-roundtrip-${user.name}.dsv
* *DSV_TARGET_TABLE = t
CREATE TABLE t (i INT, a INT, d DATE);

INSERT INTO t(i, a, d) VALUES (1, 149, null);
INSERT INTO t(i, a, d) VALUES (2, null, '2007-06-24');

/* Export */
\x t

SELECT count(*) FROM t WHERE i = 1 AND a = 149 AND d IS null;
*if (*? != 1)
    \q Pre-check of inserted data failed (1)
*end if
SELECT count(*) FROM t WHERE i = 2 AND a IS NULL AND d = '2007-06-24';
*if (*? != 1)
    \q Pre-check of inserted data failed (2)
*end if


/* Import */
\m *{*DSV_TARGET_FILE}

SELECT count(*) FROM t WHERE i = 1 AND a = 149 AND d IS null;
*if (*? != 2)
    \q Post-check of imported data failed (1)
*end if
SELECT count(*) FROM t WHERE i = 2 AND a IS NULL AND d = '2007-06-24';
*if (*? != 2)
    \q Post-check of imported data failed (2)
*end if
