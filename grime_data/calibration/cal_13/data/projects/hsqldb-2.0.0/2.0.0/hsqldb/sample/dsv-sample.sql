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
 * $Id: dsv-sample.sql 610 2008-12-22 15:54:18Z unsaved $
 *
 * Imports delimiter-separated-values, and generates an output 
 * reject .dsv file, and a reject report.
 *
 * To execute, set up a SqlTool database urlid (see User Guide if you don't
 * know how to do that); then (from this directory) execute this script like
 *
 *    java ../lib/hsqldb.jar mem dsv-sample.sql
 *
 * (replace "mem" with your urlid).
 */

CREATE TABLE sampletable(i INT, d DATE NOT NULL, b BOOLEAN);

/* If you dont' set *DSV_TARGET_TABLE, it defaults to the base name of the
   .dsv file. */
* *DSV_TARGET_TABLE = sampletable

\p WARNING:  Some records will be skipped, and some others will be rejected.
\p This is on purpose, so you can work with a reject report.
\p

/* By default, no reject files are written, and the import will abort upon
 * the first error encountered.  If you set either of these settings, the
 * import will continue to completion if at all possible. */
* *DSV_REJECT_FILE = ${java.io.tmpdir}/sample-reject.dsv
* *DSV_REJECT_REPORT = ${java.io.tmpdir}/sample-reject.html
\m sample.dsv

/* Enable this line if you want to display all successfully imported data:
SELECT * FROM sampletable;
*/

\p
\p See import reject report at '*{*DSV_REJECT_REPORT}'.
