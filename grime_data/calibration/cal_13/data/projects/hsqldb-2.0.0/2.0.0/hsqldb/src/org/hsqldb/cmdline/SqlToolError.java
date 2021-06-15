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

package org.hsqldb.cmdline;

import org.hsqldb.lib.AppendableException;

/**
 * Exceptions thrown by the SqlTool system externally to SqlFile.
 * (As opposed to the nested Exceptions within those classes).
 * This class is misnamed, because it is not only errors.
 * When there is time, this file and class should be renamed.
 * <P>
 * This class has a misleading name and should really be renamed.
 * It is Java Exception, not a Java Error.
 * </P>
 */
public class SqlToolError extends AppendableException {

    static final long serialVersionUID = 1792522673702223649L;

    public SqlToolError(Throwable cause) {
        super(null, cause);
    }

    public SqlToolError() {
        // Purposefully empty
    }

    public SqlToolError(String s) {
        super(s);
    }

    public SqlToolError(String string, Throwable cause) {
        super(string, cause);
    }
}
