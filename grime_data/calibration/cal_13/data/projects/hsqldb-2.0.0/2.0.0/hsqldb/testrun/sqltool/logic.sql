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
 * $Id: logic.sql 610 2008-12-22 15:54:18Z unsaved $
 *
 * Logic tests
 */

*if (1)
    * T1 = true
*end if
*if (! *T1)
    \q Test of (1) failed
*end if
*if (0)
    \q Test of (0) failed
*end if

*if (! 1)
    \q Test of (! 1) failed
*end if
*if (! 0)
    * T2 = true
*end if
*if (! *T2)
    \q Test of (! 0) failed
*end if

*if (!1)
    \q Test of (!1) failed
*end if
*if (!0)
    * T3 = true
*end if
*if (!*T3)
    \q Test of (!0) failed
*end if

* SETVAR=3
*if (*SETVAR)
    * T4 = true
*end if
*if (! *T4)
    \q Test of (*SETVAR) failed
*end if
*if (*UNSETVAR)
    \q Test of (*UNSETVAR) failed
*end if

*if (! *SETVAR)
    \q Test of (! *SETVAR) failed
*end if
*if (! *UNSETVAR)
    * T5 = true
*end if
*if (! *T5)
    \q Test of (! *UNSETVAR) failed
*end if

*if (!*SETVAR)
    \q Test of (!*SETVAR) failed
*end if
*if (!*UNSETVAR)
    * T6 = true
*end if
*if (!*T6)
    \q Test of (!*UNSETVAR) failed
*end if
