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

import java.io.File;
import java.io.IOException;

/* $Id: BasicResolver.java 610 2008-12-22 15:54:18Z unsaved $ */

/**
 * Resolves paths using a parent directory; does not resolve properties.
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
class BasicResolver implements IResolver {
    File parentDir;

    public BasicResolver(File parentDir) {
        this.parentDir = parentDir;
    }

    public String resolveProperties(String expression) {
        return expression;
    }
    public File resolveFile(String path) {
        File file = new File(path);

        if (parentDir != null && !file.isAbsolute()) {
            try {
                path = this.parentDir.getCanonicalPath()
                       + File.separatorChar
                       + path;

                file = new File(path);
            } catch (IOException ex) {
                path = this.parentDir.getAbsolutePath()
                       + File.separatorChar
                       + path;

                file = new File(path);
            }
        }

        try {
            return file.getCanonicalFile();
        } catch (Exception e) {
            return file.getAbsoluteFile();
        }
    }
}
