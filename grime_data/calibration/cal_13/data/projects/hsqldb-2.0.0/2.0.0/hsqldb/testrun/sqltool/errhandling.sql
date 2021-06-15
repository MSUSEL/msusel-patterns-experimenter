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
 * $Id: errhandling.sql 610 2008-12-22 15:54:18Z unsaved $
 *
 * Tests error handling inside of blocks and in subscripts.
 */

\c true
bad;
\p Made it past first ignored error.

/* At root level, can even ignore fatal syntax errors */
* foreach bad
* ifff

* V = changethis
* if (1)
    bad;
    * ifff
    * end nosuch
    * V = ok
* end if 

* if (*V != ok)
    \q Seems to have aborted inside if block in Continue-on-error mode
* end if

* W = changethis
* if (1)
    bad;
    *if (2)
        * ifff
        * end nosuch
        * W = ok
    *end if
    worse;
* end if 

* if (*W != ok)
    \q Seems to have aborted inside nested if block in Continue-on-error mode
* end if

\i errhandling.isql
