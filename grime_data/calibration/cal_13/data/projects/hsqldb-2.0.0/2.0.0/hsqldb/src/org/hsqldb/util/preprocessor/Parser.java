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

/* $Id: Parser.java 610 2008-12-22 15:54:18Z unsaved $ */

/**
 * Simple preprocessor directive parser. <p>
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
class Parser  {

    Defines   defines;
    Tokenizer tokenizer;

    Parser(Defines defines, Tokenizer tokenizer) {
        this.defines   = defines;
        this.tokenizer = tokenizer;
    }

    boolean parseExpression() throws PreprocessorException {
        boolean result = parseTerm();

        while (true) {
            switch(this.tokenizer.getTokenType()) {
                case Token.OR : {
                    this.tokenizer.next();

                    result = result | parseTerm();

                    break;
                }
                case Token.XOR : {
                    this.tokenizer.next();

                    result = result ^ parseTerm();

                    break;
                }

                default : {
                    return result;
                }
            }
        }
    }

    boolean parseTerm() throws PreprocessorException {
        boolean result = parseFactor();

        while (this.tokenizer.isToken(Token.AND)) {
            this.tokenizer.next();

            result = result & parseFactor();
        }

        return result;
    }

    boolean parseFactor() throws PreprocessorException {
        boolean result;

        switch(this.tokenizer.getTokenType()) {
            case Token.IDENT : {
                String ident = this.tokenizer.getIdent();
                int    type  = this.tokenizer.next();

                if ((type == Token.EOI) || (type == Token.RPAREN) ||
                        Token.isLogicalOperator(type)) {
                    result = this.defines.isDefined(ident);
                } else if (Token.isComparisonOperator(type)) {
                    result = parseComparison(ident, type);
                } else {
                    throw new PreprocessorException("Logical or comparison "
                            + "operator token required at position "
                            + this.tokenizer.getStartIndex()
                            + " in ["
                            + this.tokenizer.getSource()
                            + "]"); // NOI18N
                }

                break;
            }
            case Token.NOT :{
                this.tokenizer.next();

                result = !parseFactor();

                break;
            }
            case Token.LPAREN : {
                this.tokenizer.next();

                result = parseExpression();

                if (!this.tokenizer.isToken(Token.RPAREN)) {
                    throw new PreprocessorException("RPAREN token required at "
                            + "position "
                            + this.tokenizer.getStartIndex()
                            + " in ["
                            + this.tokenizer.getSource()
                            + "]"); // NOI18N
                }

                this.tokenizer.next();

                break;
            }
            default : {
                throw new PreprocessorException("IDENT, NOT or LPAREN "
                        + "token required at position "
                        + this.tokenizer.getStartIndex()
                        + " in ["
                        + this.tokenizer.getSource()
                        + "]"); // NOI18N
            }
        }

        return result;
    }

    boolean parseComparison(String ident, int opType)
    throws PreprocessorException {
//        checkIsComparisonOperator(opType);

        boolean result;
        Object  lhs    = this.defines.getDefintion(ident);
        int     pos    = this.tokenizer.getStartIndex();
        Object  rhs    = parseValue();

        if (lhs == null) {
            throw new PreprocessorException("IDENT " + ident
                    + " is not defined at position"
                    + pos
                    + "in ["
                    + this.tokenizer.getSource()
                    + "]"); // NOI18N
        }

        switch(opType) {
            case Token.EQ :{
                result = (compare(lhs, rhs) == 0);

                break;
            }
            case Token.LT : {
                result = (compare(lhs, rhs) < 0);

                break;
            }
            case Token.LTE : {
                result = (compare(lhs, rhs) <= 0);

                break;
            }
            case Token.GT : {
                result = (compare(lhs, rhs) > 0);

                break;
            }
            case Token.GTE : {
                result = (compare(lhs, rhs) >= 0);

                break;
            }
            default : {
                // Stupid compiler trick.
                // Can't actually happen because this case will cause an
                // exception to be thrown in method parseFactor (or in
                // method checkIsComparisonOperator when uncommented)
                throw new PreprocessorException("Internal error"); // NOI18N
            }
        }

        this.tokenizer.next();

        return result;
    }

//    void checkIsComparisonOperator(int opType) throws PreprocessorException {
//        if (!Token.isComparisonOperator(opType)) {
//                throw new PreprocessorException("Comparison "
//                        + "operator token required at position "
//                        + tokenizer.getBeginIndex()
//                        + " in ["
//                        + tokenizer.getSource()
//                        + "]"); // NOI18N
//        }
//    }

    static int compare(Object o1, Object o2) {
        // nulls are basically 'illegal' so yes: 
        // we want to throw NPE here if o1 or o2 is null
        if (o1 instanceof Comparable) {
            return (o1.getClass().isAssignableFrom(o2.getClass()))
            ? ((Comparable)o1).compareTo(o2)
            : String.valueOf(o1).compareTo(String.valueOf(o2));
        } else {
            return o1.toString().compareTo(o2.toString());
        }
    }

    Object parseValue() throws PreprocessorException {
        Object value;

        switch(this.tokenizer.next()) {
            case Token.IDENT : {
                String ident = this.tokenizer.getIdent();

                value = this.defines.getDefintion(ident);

                if (value == null) {
                    throw new PreprocessorException("IDENT " + ident
                            + " is not defined at position"
                            + this.tokenizer.getStartIndex()
                            + "in ["
                            + this.tokenizer.getSource()
                            + "]"); // NOI18N
                }

                break;
            }
            case Token.STRING : {
                value = this.tokenizer.getString();

                break;
            }
            case Token.NUMBER : {
                value = this.tokenizer.getNumber();

                break;
            }
            default :{
                throw new PreprocessorException("IDENT, STRING"
                        + "or NUMBER token required at position "
                        + this.tokenizer.getStartIndex()
                        + " in: ["
                        + this.tokenizer.getSource()
                        + "]"); // NOI18N
            }
        }

        return value;
    }
}
