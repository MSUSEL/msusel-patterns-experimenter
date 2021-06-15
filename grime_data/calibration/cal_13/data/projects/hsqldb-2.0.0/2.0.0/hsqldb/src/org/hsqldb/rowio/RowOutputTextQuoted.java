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

import org.hsqldb.lib.StringConverter;

/**
 * This class quotes strings only if they contain the quote character or
 * the separator for the field. The quote character is doubled.
 *
 * @author Bob Preston (sqlbob@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.0
 */
public class RowOutputTextQuoted extends RowOutputText {

    public RowOutputTextQuoted(String fieldSep, String varSep,
                               String longvarSep, boolean allQuoted,
                               String encoding) {
        super(fieldSep, varSep, longvarSep, allQuoted, encoding);
    }

    protected String checkConvertString(String s, String sep) {

        if (allQuoted || s.length() == 0 || s.indexOf('\"') != -1
                || (sep.length() > 0 && s.indexOf(sep) != -1)
                || hasUnprintable(s)) {
            s = StringConverter.toQuotedString(s, '\"', true);
        }

        return s;
    }

    private static boolean hasUnprintable(String s) {

        for (int i = 0, len = s.length(); i < len; i++) {
            if (Character.isISOControl(s.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
