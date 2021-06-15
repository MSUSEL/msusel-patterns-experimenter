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


-- Sample file used to test import feature of multiline SQL script (HHH-2403).
-- Contains various SQL instructions with comments.

CREATE TABLE test_data (
  id    NUMBER        NOT NULL   PRIMARY KEY -- primary key
, text  VARCHAR2(100)                        /* any other data */
);

INSERT INTO test_data VALUES (1, 'sample');

DELETE
  FROM test_data;

/*
 * Data insertion...
 */
INSERT INTO test_data VALUES (2, 'Multi-line comment line 1
-- line 2''
/* line 3 */');

/* Invalid insert: INSERT INTO test_data VALUES (1, NULL); */
-- INSERT INTO test_data VALUES (1, NULL);

INSERT INTO test_data VALUES (3 /* 'third record' */, NULL /* value */); -- with NULL value
INSERT INTO test_data (id, text)
     VALUES
          (
            4 -- another record
          , NULL
          );

