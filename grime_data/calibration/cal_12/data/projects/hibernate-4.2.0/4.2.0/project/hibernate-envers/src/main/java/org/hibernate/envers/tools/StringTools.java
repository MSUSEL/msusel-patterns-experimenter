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
package org.hibernate.envers.tools;
import java.util.Iterator;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class StringTools {
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isEmpty(Object o) {
        return o == null || "".equals(o);
    }

    /**
     * @param s String, from which to get the last component.
     * @return The last component of the dot-separated string <code>s</code>. For example, for a string
     * "a.b.c", the result is "c".
     */
    public static String getLastComponent(String s) {
        if (s == null) {
            return null;
        }

        int lastDot = s.lastIndexOf(".");
        if (lastDot == -1) {
            return s;
        } else {
            return s.substring(lastDot + 1);
        }
    }

    /**
     * To the given string builder, appends all strings in the given iterator, separating them with the given
     * separator. For example, for an interator "a" "b" "c" and separator ":" the output is "a:b:c".
     * @param sb String builder, to which to append.
     * @param contents Strings to be appended.
     * @param separator Separator between subsequent content.
     */
    public static void append(StringBuilder sb, Iterator<String> contents, String separator) {
        boolean isFirst = true;

        while (contents.hasNext()) {
            if (!isFirst) {
                sb.append(separator);
            }

            sb.append(contents.next());
            
            isFirst = false;
        }
    }
}
