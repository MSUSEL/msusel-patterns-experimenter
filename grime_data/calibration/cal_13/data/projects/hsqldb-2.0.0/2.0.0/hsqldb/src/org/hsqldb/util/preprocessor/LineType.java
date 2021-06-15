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

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Locale;

/* $Id: LineType.java 610 2008-12-22 15:54:18Z unsaved $ */

/**
 * Static methods and constants to decode preprocessor line types.
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
class LineType {
    //
    static final int UNKNOWN    = 0;
    static final int DEF        = 1;
    static final int DEFINE     = 1;
    static final int ELIF       = 2;
    static final int ELIFDEF    = 3;
    static final int ELIFNDEF   = 4;
    static final int ELSE       = 5;
    static final int ENDIF      = 6;
    static final int ENDINCLUDE = 7;
    static final int HIDDEN     = 8;
    static final int IF         = 9;
    static final int IFDEF      = 10;
    static final int IFNDEF     = 11;
    static final int INCLUDE    = 12;
    static final int UNDEF      = 13;
    static final int UNDEFINE   = 13;
    static final int VISIBLE    = 14;

    //
    private static Hashtable directives;
    private static String[]  labels;

    static synchronized String[] labels() {
        if (labels == null) {
            init();
        }

        return labels;
    }

    static synchronized Hashtable directives() {
        if (directives == null) {
            init();
        }

        return directives;
    }

    private static void init() {

        directives     = new Hashtable();
        labels         = new String[17];
        Field[] fields = LineType.class.getDeclaredFields();

        for (int i = 0, j = 0; i < fields.length; i++) {
            Field field = fields[i];

            if (field.getType().equals(Integer.TYPE)) {
                String label = field.getName();

                try {
                    int value = field.getInt(null);

                    labels[value] = label;

                    switch(value) {
                        case VISIBLE :
                        case HIDDEN : {
                            // ignore
                            break;
                        }
                        default : {
                            String key = Line.DIRECTIVE_PREFIX
                                    + label.toLowerCase(Locale.ENGLISH);

                            directives.put(key, new Integer(value));

                            break;
                        }
                    }

                } catch (IllegalArgumentException ex) {
                    // ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    // ex.printStackTrace();
                }
            }
        }
    }
}
