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
package com.gargoylesoftware.htmlunit.util;

import java.io.File;

/**
 * A holder for a key/value pair that represents a file to upload.
 *
 * @version $Revision: 5301 $
 * @author Brad Clarke
 * @author David D. Kilzer
 * @author Mike Bowler
 */
public class KeyDataPair extends NameValuePair {

    private static final long serialVersionUID = -1129314696176851675L;

    private final File fileObject_;
    private final String contentType_;
    private final String charset_;
    private byte[] data_;

    /**
     * Creates an instance.
     *
     * @param key the key
     * @param file the file
     * @param contentType the content type
     * @param charset the charset encoding
     */
    public KeyDataPair(final String key, final File file, final String contentType,
            final String charset) {

        super(key, file.getName());

        if (file.exists()) {
            fileObject_ = file;
        }
        else {
            fileObject_ = null;
        }

        contentType_ = contentType;
        charset_ = charset;
    }

    /**
     * @return the {@link File} object if the file exists, else <tt>null</tt>
     */
    public File getFile() {
        return fileObject_;
    }

    /**
     * Gets the charset encoding for this file upload.
     * @return the charset
     */
    public String getCharset() {
        return charset_;
    }

    /**
     * Gets the content type for this file upload.
     * @return the content type
     */
    public String getContentType() {
        return contentType_;
    }

    /**
     * Gets in-memory data assigned to file value.
     * @return <code>null</code> if the file content should be used.
     */
    public byte[] getData() {
        return data_;
    }

    /**
     * Sets file value data. If nothing is set, the file content will be used.
     * @param data byte array with file data.
     */
    public void setData(final byte[] data) {
        data_ = data;
    }
}
