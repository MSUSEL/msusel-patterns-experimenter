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

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for abstraction of file access.
 *
 * @author  Ocke Janssen oj@openoffice.org
 * @version 1.9.0
 * @since 1.8.0
 */
public interface FileAccess {

    int ELEMENT_READ         = 1;
    int ELEMENT_SEEKABLEREAD = 3;
    int ELEMENT_WRITE        = 4;
    int ELEMENT_READWRITE    = 7;
    int ELEMENT_TRUNCATE     = 8;

    InputStream openInputStreamElement(java.lang.String streamName)
    throws java.io.IOException;

    OutputStream openOutputStreamElement(java.lang.String streamName)
    throws java.io.IOException;

    boolean isStreamElement(java.lang.String elementName);

    void createParentDirs(java.lang.String filename);

    void removeElement(java.lang.String filename);

    void renameElement(java.lang.String oldName, java.lang.String newName);

    public interface FileSync {
        void sync() throws java.io.IOException;
    }

    FileSync getFileSync(OutputStream os) throws java.io.IOException;
}
