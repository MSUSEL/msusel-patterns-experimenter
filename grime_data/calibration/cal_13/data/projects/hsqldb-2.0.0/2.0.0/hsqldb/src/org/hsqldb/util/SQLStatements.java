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

package org.hsqldb.util;

import java.io.Serializable;

/**
 * container for set of SQL statements
 *
 * New class based on Hypersonic SQL code.
 *
 * @version 1.7.1
 * @since 1.7.1
 */
class SQLStatements implements Serializable {

    String  sSchema, sType;
    String  sDatabaseToConvert;
    String  sSourceTable, sDestTable;
    String  sDestDrop, sDestCreate, sDestInsert, sDestDelete;
    String  sDestDropIndex, sDestCreateIndex, sDestAlter, sSourceSelect;
    boolean bTransfer    = true;
    boolean bCreate      = true;
    boolean bDelete      = true;
    boolean bDrop        = true;
    boolean bCreateIndex = true;
    boolean bDropIndex   = true;
    boolean bInsert      = true;
    boolean bAlter       = true;
    boolean bFKForced    = false;
    boolean bIdxForced   = false;
}
