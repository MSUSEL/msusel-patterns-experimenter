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
package com.gargoylesoftware.htmlunit;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;

/**
 * Wrapper for content downloaded from a remote server.
 *
 * @version $Revision: 5844 $
 * @author Marc Guillemot
 */
public interface DownloadedContent extends Serializable {
    /**
     * Implementation keeping content in memory.
     */
    static class InMemory implements DownloadedContent {
        private static final long serialVersionUID = -3297925685606468344L;
        private final byte[] bytes_;
        public InMemory(final byte[] byteArray) {
            if (byteArray == null) {
                bytes_ = ArrayUtils.EMPTY_BYTE_ARRAY;
            }
            else {
                bytes_ = byteArray;
            }
        }

        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes_);
        }
    }

    /**
     * Implementation keeping content on the file system.
     */
    static class OnFile implements DownloadedContent {
        private static final long serialVersionUID = 8385310409274828198L;
        private final File file_;
        public OnFile(final File file) {
            file_ = file;
        }

        public InputStream getInputStream() throws FileNotFoundException {
            return new FileInputStream(file_);
        }
    }

    /**
     * Returns a new {@link InputStream} allowing to read the downloaded content.
     * @return the InputStream
     * @throws IOException in case of problem accessing the content
     */
    InputStream getInputStream() throws IOException;
}
