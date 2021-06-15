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
 * $Id: macro.sql 610 2008-12-22 15:54:18Z unsaved $
 *
 * Tests interactive macro commands /...
 *
 * See macro.inter script also.
 */


\c false

/=q SELECT count(*) FROM t

CREATE TABLE t(i int);
INSERT INTO t VALUES(1);
INSERT INTO t VALUES(2);
INSERT INTO t VALUES(3);
INSERT INTO t VALUES(4);
INSERT INTO t VALUES(5);
COMMIT;

/q WHERE i > 3;

* if (*? != 2)
    \q Query returned *{?} rows, should have returned 2.
* end if

/* Now test with lots of intervening white space */
  /  =   q   SELECT count(*) FROM t WHERE i > *{LIMIT}   

   /   =   setlim  *  LIMIT =   

* LIMIT = 4
/q;
* if (*? != 1)
    \q Query returned *{?} rows, should have returned 1.
* end if

/setlim 3;
/q;
* if (*? != 2)
    \q Query returned *{?} rows, should have returned 2.
* end if
