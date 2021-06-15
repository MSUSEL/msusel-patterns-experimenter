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

package org.hsqldb.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows additional messages to be appended.
 *
 * It often makes for better (and more efficient) design to add context
 * details to an exception at intermediate points in the thread.
 * This class makes it easy and efficient to catch and rethrow for that purpose.
 */
public class AppendableException extends Exception {

    static final long    serialVersionUID = -1002629580611098803L;
    public static String LS = System.getProperty("line.separator");
    public List<String>  appendages       = null;

    public String getMessage() {

        String message = super.getMessage();

        if (appendages == null) {
            return message;
        }

        StringBuffer sb = new StringBuffer();

        if (message != null) {
            sb.append(message);
        }

        for (String appendage : appendages) {
            if (sb.length() > 0) {
                sb.append(LS);
            }

            sb.append(appendage);
        }

        return sb.toString();
    }

    public void appendMessage(String s) {

        if (appendages == null) {
            appendages = new ArrayList<String>();
        }

        appendages.add(s);
    }

    public AppendableException() {
        // Intentionally empty
    }

    public AppendableException(String s) {
        super(s);
    }

    public AppendableException(Throwable cause) {
        super(cause);
    }

    public AppendableException(String string, Throwable cause) {
         super(string, cause);
    }
}
