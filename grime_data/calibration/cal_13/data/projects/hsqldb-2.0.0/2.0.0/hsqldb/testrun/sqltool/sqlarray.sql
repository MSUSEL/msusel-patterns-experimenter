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
 * $Id: sqlarray.sql 3615 2010-06-02 11:17:43Z unsaved $
 *
 * Tests basic usage of SQL Arrays
 */

CREATE TABLE a (i BIGINT PRIMARY KEY, ar INTEGER ARRAY);

INSERT INTO a VALUES (1, array [11, null, 13]);
INSERT INTO a VALUES (2, null);
INSERT INTO a VALUES (3, array [21, 22]);

* ROWCOUNT _
SELECT count(*) FROM a;

* if (*ROWCOUNT != 3)
    \q Failed to insert 3 rows with SQL Values
* end if

* ROWCOUNT _
 SELECT count(*) FROM a WHERE i = 1 AND ar[3] = 13;
* if (*ROWCOUNT != 1)
    \q Failed to read imported SQL Array Element
* end if
