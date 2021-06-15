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

import java.util.Hashtable;

/* $Id: Defines.java 610 2008-12-22 15:54:18Z unsaved $ */

/**
 * Simple preprocessor symbol table.
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
class Defines {
    private Hashtable symbols = new Hashtable();

    public Defines() {}

    public Defines(String csvExpressions) throws PreprocessorException {
        defineCSV(csvExpressions);
    }

    public void clear() {
        this.symbols.clear();
    }

    public void defineCSV(String csvExpressions)
    throws PreprocessorException {
        if (csvExpressions != null) {
            csvExpressions = csvExpressions + ',';

            int start = 0;
            int len   = csvExpressions.length();

            while (start < len) {
                int    end  = csvExpressions.indexOf(',', start);
                String expr = csvExpressions.substring(start, end).trim();

                if (expr.length() > 0) {
                    defineSingle(expr);
                }

                start = end + 1;
            }
        }
    }

    public void defineSingle(String expression) throws PreprocessorException {
        Tokenizer tokenizer = new Tokenizer(expression);

        tokenizer.next();

        if (!tokenizer.isToken(Token.IDENT)) {
            throw new PreprocessorException("IDENT token required at position: "
                    + tokenizer.getStartIndex()
                    + " in ["
                    + expression +
                    "]"); // NOI18N
        }

        String ident = tokenizer.getIdent();

        int tokenType = tokenizer.next();

        switch(tokenType) {
            case Token.EOI : {
                this.symbols.put(ident, ident);
                return;
            }
            case Token.ASSIGN : {
                tokenType = tokenizer.next();
                break;
            }
            default : {
                break;
            }
        }

        switch(tokenType) {
            case Token.NUMBER : {
                Number number = tokenizer.getNumber();

                this.symbols.put(ident, number);

                break;
            }
            case Token.STRING : {
                String string = tokenizer.getString();

                this.symbols.put(ident, string);

                break;
            }
            case Token.IDENT : {
                String rhsIdent = tokenizer.getIdent();

                if (!isDefined(rhsIdent)) {
                    throw new PreprocessorException("Right hand side" +
                            "IDENT token [" + rhsIdent + "] at position: "
                            + tokenizer.getStartIndex()
                            + " is undefined in ["
                            + expression
                            + "]"); // NOI18N
                }

                Object value = this.symbols.get(rhsIdent);

                symbols.put(ident, value);
                break;
            }
            default : {
                throw new PreprocessorException("Right hand side NUMBER,"
                        + "STRING or IDENT token required at position: " +
                        + tokenizer.getStartIndex()
                        + " in ["
                        + expression
                        + "]"); // NOI18N
            }
        }

        tokenizer.next();

        if (!tokenizer.isToken(Token.EOI)) {
            throw new PreprocessorException("Illegal trailing "
                    + "characters at position: "
                    + tokenizer.getStartIndex()
                    + " in ["
                    + expression
                    + "]"); // NOI18N
        }

        return;
    }

    public void undefine(String symbol) {
        this.symbols.remove(symbol);
    }

    public boolean isDefined(String symbol) {
        return this.symbols.containsKey(symbol);
    }

    public Object getDefintion(String symbol) {
        return this.symbols.get(symbol);
    }

    public boolean evaluate(String expression) throws PreprocessorException {
        Tokenizer tokenizer = new Tokenizer(expression);

        tokenizer.next();

        Parser parser = new Parser(this, tokenizer);

        boolean result = parser.parseExpression();

        if (!tokenizer.isToken(Token.EOI)) {
            throw new PreprocessorException("Illegal trailing "
                    + "characters at position: "
                    + tokenizer.getStartIndex()
                    + " in ["
                    + expression
                    + "]"); // NOI18N
        }

        return result;
    }

    public String toString() {
        return super.toString() + this.symbols.toString();
    }
}
