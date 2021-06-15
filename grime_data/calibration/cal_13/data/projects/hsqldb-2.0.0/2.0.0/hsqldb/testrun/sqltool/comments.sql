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
 * $Id: comments.sql 610 2008-12-22 15:54:18Z unsaved $
 *
 * Tests comments.  This comment itself is a multi-line comment
 */

/* Just to have a work table */
CREATE TABLE t(i int);

  /* A multi-line
  comment  with
  leading + trailing white space. */  

/*Repeat with text right up to edges.
 *
 * Tests comments.  This comment itself is a multi-line comment*/

  /*Repeat with text right up to edges.
  comment  with
  leading + trailing white space. */

/* Following line contains spaces */
           

/* Simple hyphen-hyphen comments */
-- blah
  -- blah

/* Empty and white space comments: */
/**/
/*****/
/*** Extra stars ***/
  /*** Extra stars ***/   
   /**************/
---- Extra slashes
  ---- Extra slashes  
    ----------------------
/*  */
  /**/  
  /*  */
/* The second of each of the following pairs have trailing white space.
--
--  
  --
  --  

/* Comments trailing SQL */
INSERT INTO t VALUES (9);

/* Simple SQL-embedded traditional comments */
SELECT * FROM  /* A simple traditional comment */ t;
* if (*? != 9)
    \q Hyphen-hyphen trailing SQL failed
* end if
SELECT * FROM  /* A simple traditional
comment */ t;
* if (*? != 9)
    \q Hyphen-hyphen trailing SQL failed
* end if
SELECT * FROM  -- A simple single-line comment
t;
* if (*? != 9)
    \q Hyphen-hyphen trailing SQL failed
* end if
SELECT * FROM  -- Two simple single-line
-- comments
t;
* if (*? != 9)
    \q Hyphen-hyphen trailing SQL failed
* end if
SELECT * FROM  -- Two simple single-line
  -- comments.  With leading white space
  t;
* if (*? != 9)
    \q Hyphen-hyphen trailing SQL failed
* end if

/* Nesting different comments inside one another */
/* -- The traditional comment should still close. */
SELECT * FROM  /* Something -- single-line SQL-trailing comment */ t;
* if (*? != 9)
    \q Hyphen-hyphen trailing SQL failed
* end if

/* Sanity check */
* V1 = one
* if (*V1 != one)
  \q Failed sanity check for simple variable test
* end if

/* Test single-line within PL command */
* V2 = alpha--some crap
* if (*V2 != alpha)
  \q Failed single-line within PL command
* end if

/* Test traditional within PL command */
* V3 = gamma/*some crap*/
* if (*V3 != gamma)
  \q Failed traditional within PL command
* end if

/* Test multiple traditionals within PL command */
* V4 = de/*some crap*/l/*more*/ta
* if (*V4 != delta)
  \q Failed multiple traditional within PL command
* end if

/* Test single-line within PL command */
* V5 = alpha--some crap /* with nested traditional */ there
* if (*V5 != alpha)
  \q Failed single-line within PL w/ nesting+trailing failed
* end if

/* Test single-line within PL command */
* V6 = alpha--some crap /* with nested traditional */
* if (*V6 != alpha)
  \q Failed single-line within PL w/ nesting failed
* end if

/* Test single-line within PL command */
* V7 = alpha--some crap /* with nested traditional
* if (*V6 != alpha)
  \q Failed single-line within PL w/ unclosed nesting failed
* end if
