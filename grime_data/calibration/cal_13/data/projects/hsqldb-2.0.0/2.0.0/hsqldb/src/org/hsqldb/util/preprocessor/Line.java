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

/* $Id: Line.java 610 2008-12-22 15:54:18Z unsaved $ */

/**
 * Preprocessor's view of a line in a text document.
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
class Line  {
    static final String DIRECTIVE_PREFIX = "//#";
    static final String SPACE_CHARS = " \t";
    static final int    DIRECTIVE_PREFIX_LENGTH = DIRECTIVE_PREFIX.length();
    static final int    DIRECTIVE_PREFIX_LENGTH_PLUS_ONE =
            DIRECTIVE_PREFIX_LENGTH + 1;
    static final String HIDE_DIRECTIVE = DIRECTIVE_PREFIX + ' ';

    int    type;
    String sourceText;
    String indent;
    String text;
    String arguments;

    static int indexOfNonTabOrSpace(String line) {
        int pos = 0;
        int len = line.length();

        while (pos < len) {
            char ch = line.charAt(pos);

            if ((ch == ' ') || (ch == '\t')) {
                pos++;
                continue;
            }

            break;
        }

        return pos;
    }

    static int indexOfTabOrSpace(String s, int fromIndex) {
        int spos = s.indexOf(' ', fromIndex);
        int tpos = s.indexOf('\t', fromIndex);

        return (((tpos != -1) && (tpos < spos)) || (spos == -1)) ? tpos : spos;
    }

    Line(String line) throws PreprocessorException {
        setSourceText(line);
    }

    void setSourceText(String line) throws PreprocessorException {
        this.sourceText = line;
        int pos         = indexOfNonTabOrSpace(line);
        this.indent     = line.substring(0, pos);
        line            = line.substring(pos);

        if (!line.startsWith(DIRECTIVE_PREFIX)) {
            this.text      = line;
            this.arguments = null;
            this.type      = LineType.VISIBLE;
        } else if (line.length() == DIRECTIVE_PREFIX_LENGTH){
            this.text      = "";
            this.arguments = null;
            this.type       = LineType.HIDDEN;
        } else  if (SPACE_CHARS.indexOf(line.
                charAt(DIRECTIVE_PREFIX_LENGTH)) != -1) {
            this.text      = line.substring(DIRECTIVE_PREFIX_LENGTH_PLUS_ONE);
            this.arguments = null;
            this.type      = LineType.HIDDEN;
        } else {
            pos = indexOfTabOrSpace(line, DIRECTIVE_PREFIX_LENGTH_PLUS_ONE);

            if (pos == -1) {
                this.text      = line;
                this.arguments = null;
            } else {
                this.text      = line.substring(0, pos);
                this.arguments = line.substring(pos + 1).trim();
            }

            Integer oType = (Integer) LineType.directives().get(text);

            if (oType == null) {
                throw new PreprocessorException("Unknown directive ["
                        + text + "] in [" + line + "]"); // NOI18N
            }

            this.type = oType.intValue();
        }

    }

    String getArguments() throws PreprocessorException {
        if (arguments == null || arguments.length() == 0) {
            throw new PreprocessorException("["+ text
                    + "]: has no argument(s)"); // NOI18N
        }

        return arguments;
    }

    String getSourceText() {
        return sourceText;
    }

    String getIndent() {
        return indent;
    }

    String getText() {
        return text;
    }

    int getType() {
        return type;
    }

    boolean isType(int lineType) {
        return (this.type == lineType);
    }

    public String toString() {
        return LineType.labels()[this.type] + "(" + this.type + "): indent ["
                + this.indent + "] text [" + this.text
                + ((this.arguments == null) ? "]" : ("] args ["
                + this.arguments + "]")) ;
    }
}
