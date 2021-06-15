/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.hsqldb;

public interface OpTypes {

    int VALUE                = 1,     // constant value
        COLUMN               = 2,     // references
        COALESCE             = 3,
        DEFAULT              = 4,
        SIMPLE_COLUMN        = 5,
        VARIABLE             = 6,
        PARAMETER            = 7,
        DYNAMIC_PARAM        = 8,
        ASTERISK             = 9,
        SEQUENCE             = 10,
        ARRAY                = 19,
        MULTISET             = 20,
        SCALAR_SUBQUERY      = 21,    // query based row or table
        ROW_SUBQUERY         = 22,
        TABLE_SUBQUERY       = 23,
        ROW                  = 25,    // rows
        TABLE                = 26,
        FUNCTION             = 27,
        SQL_FUNCTION         = 28,
        ROUTINE_FUNCTION     = 29,
        NEGATE               = 31,    // arithmetic operations
        ADD                  = 32,
        SUBTRACT             = 33,
        MULTIPLY             = 34,
        DIVIDE               = 35,
        CONCAT               = 36,    // concatenation
        EQUAL                = 41,    // logical - comparison
        GREATER_EQUAL        = 42,
        GREATER              = 43,
        SMALLER              = 44,
        SMALLER_EQUAL        = 45,
        NOT_EQUAL            = 46,
        IS_NULL              = 47,
        NOT                  = 48,    // logical operations
        AND                  = 49,
        OR                   = 50,
        ALL_QUANTIFIED       = 51,    // logical - quantified comparison
        ANY_QUANTIFIED       = 52,
        LIKE                 = 53,    // logical - predicates
        IN                   = 54,
        EXISTS               = 55,
        OVERLAPS             = 56,
        UNIQUE               = 57,
        NOT_DISTINCT         = 58,
        MATCH_SIMPLE         = 59,
        MATCH_PARTIAL        = 60,
        MATCH_FULL           = 61,
        MATCH_UNIQUE_SIMPLE  = 62,
        MATCH_UNIQUE_PARTIAL = 63,
        MATCH_UNIQUE_FULL    = 64,
        COUNT                = 71,    // aggregate functions
        SUM                  = 72,
        MIN                  = 73,
        MAX                  = 74,
        AVG                  = 75,
        EVERY                = 76,
        SOME                 = 77,
        STDDEV_POP           = 78,
        STDDEV_SAMP          = 79,
        VAR_POP              = 80,
        VAR_SAMP             = 81,
        CAST                 = 91,    // other operations
        ZONE_MODIFIER        = 92,
        CASEWHEN             = 93,
        ORDER_BY             = 94,
        LIMIT                = 95,
        ALTERNATIVE          = 96,
        MULTICOLUMN          = 97,
        USER_AGGREGATE       = 98,
        ARRAY_ACCESS         = 99,
        ARRAY_SUBQUERY       = 100
    ;
}
