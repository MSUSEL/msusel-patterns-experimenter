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

package org.hsqldb.rowio;

import java.io.IOException;

import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;

/**
 * Fields in the source file need not be quoted. Methods in this class unquote
 * the fields if they are quoted and handle quote character doubling in this
 * case.
 *
 * @author Bob Preston (sqlbob@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.0
 */
public class RowInputTextQuoted extends RowInputText {

    private static final int NORMAL_FIELD   = 0;
    private static final int NEED_END_QUOTE = 1;
    private static final int FOUND_QUOTE    = 2;
    private char[]           qtext;

    public RowInputTextQuoted(String fieldSep, String varSep,
                              String longvarSep, boolean allQuoted) {
        super(fieldSep, varSep, longvarSep, allQuoted);
    }

    public void setSource(String text, int pos, int byteSize) {

        super.setSource(text, pos, byteSize);

        qtext = text.toCharArray();
    }

    protected String getField(String sep, int sepLen,
                              boolean isEnd) throws IOException {

        //fredt - now the only supported behaviour is emptyIsNull
        String s = null;

        if (next >= qtext.length || qtext[next] != '\"') {
            return super.getField(sep, sepLen, isEnd);
        }

        try {
            field++;

            StringBuffer sb    = new StringBuffer();
            boolean      done  = false;
            int          state = NORMAL_FIELD;
            int          end   = -1;

            if (!isEnd) {
                end = text.indexOf(sep, next);
            }

            for (; next < qtext.length; next++) {
                switch (state) {

                    case NORMAL_FIELD :
                    default :
                        if (next == end) {
                            next += sepLen;
                            done = true;
                        } else if (qtext[next] == '\"') {

                            //-- Beginning of field
                            state = NEED_END_QUOTE;
                        } else {
                            sb.append(qtext[next]);
                        }
                        break;

                    case NEED_END_QUOTE :
                        if (qtext[next] == '\"') {
                            state = FOUND_QUOTE;
                        } else {
                            sb.append(qtext[next]);
                        }
                        break;

                    case FOUND_QUOTE :
                        if (qtext[next] == '\"') {

                            //-- Escaped quote
                            sb.append(qtext[next]);

                            state = NEED_END_QUOTE;
                        } else {
                            next  += sepLen - 1;
                            state = NORMAL_FIELD;

                            if (!isEnd) {
                                next++;

                                done = true;
                            }
                        }
                        break;
                }

                if (done) {
                    break;
                }
            }

            s = sb.toString();
        } catch (Exception e) {
            throw new IOException(
                Error.getMessage(
                    ErrorCode.M_TEXT_SOURCE_FIELD_ERROR, 0, new Object[] {
                new Integer(field), e.toString()
            }));
        }

        return s;
    }
}
