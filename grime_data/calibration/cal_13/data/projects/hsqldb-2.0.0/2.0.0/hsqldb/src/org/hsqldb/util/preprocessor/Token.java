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

package org.hsqldb.util.preprocessor;

/* $Id: Token.java 610 2008-12-22 15:54:18Z unsaved $ */

/**
 * Static methods and constants to decode directive tokens.
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
final class Token {
    static final int EOI     = -1;
    static final int UNKNOWN = 0;
    static final int IDENT   = 1;
    static final int NUMBER  = 2;
    static final int STRING  = 3;
    static final int AND     = '&';
    static final int OR      = '|';
    static final int XOR     = '^';
    static final int NOT     = '!';
    static final int GT      = '>';
    static final int GTE     = '>' + '=';
    static final int LT      = '<';
    static final int LTE     = '<' + '=';
    static final int ASSIGN  = '=';
    static final int EQ      = '=' + '=';
    static final int LPAREN  = '(';
    static final int RPAREN  = ')';

    static boolean isAssignmentOperator(final int type) {
        return (type == ASSIGN);
    }

    static boolean isComparisonOperator(final int type) {
        switch(type) {
            case EQ :
            case LT :
            case GT :
            case LTE :
            case GTE : {
                return true;
            }
            default : {
                return false;
            }
        }
    }

    static boolean isLogicalOperator(final int type) {
        switch(type) {
            case AND :
            case OR :
            case XOR :
            case NOT : {
                return true;
            }
            default : {
                return false;
            }
        }
    }

    static boolean isValue(final int type) {
        switch (type) {
            case IDENT :
            case STRING :
            case NUMBER : {
                return true;
            }
            default : {
                return false;
            }
        }
    }
}
