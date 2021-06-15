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
 * $Id: nestingschema.sql 437 2008-11-14 04:17:35Z unsaved $
 *
 * Tests a schema creation statement that nests some objects.
 */

/**
 * Besides testing nesting of commands without ; delimiters, this also
 * regression tests that the CREATE SCHEMA... command itself does not require
 * the closing ; to be sent to the DB engine.
 */
CREATE SCHEMA FELIX AUTHORIZATION DBA
    CREATE TABLE FELIXT1 (AV1 VARCHAR(10), BV VARCHAR(10))
    CREATE TABLE FELIXT2 (AV2 VARCHAR(10), BI INTEGER)
    CREATE SEQUENCE FELIXS1
    CREATE VIEW FELIXV1 AS SELECT * FROM FELIXT1 JOIN FELIXT2 ON AV1 = AV2
    CREATE VIEW FELIXV2 AS SELECT AV1 AS C1, NEXT VALUE FOR FELIXS1 AS C2 FROM FELIXT1;

SELECT count(*) FROM felix.felixv2;
*if (*? != 0)
    \q Nesting CREATE SCHEMA failed
*end if
