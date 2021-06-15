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
package com.ivata.groupware.business.drive.file;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.ivata.mask.util.SerializedByteArray;


/**
 * <p>Encapsulates a file (content & mime type) for
 * download purposes.</p>
 *
 * @since 2002-11-10
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class FileContentDO implements Serializable {
    /**
     * <p>The mime-type of the content.</p>
     */
    private String mimeType;

    /**
     * <p>Stores the content of the file, in a byte array which
     * can be passed from server-side to client-side.</p>
     */
    private SerializedByteArray content;

    /**
     * <p>Intitialize the content and mime type for this DO.
     * These class attributes are <em>immutable<em> - once they have been
     * set by
     * this constrctor, you can't change them.</p>
     *
     * @param content see {@link #getContent}.
     * @param mimeType see {@link #getMimeType}.
     *
     * @roseuid 3E228C3701FD
     */
    public FileContentDO(SerializedByteArray content, String mimeType) {
        this.content = content;
        this.mimeType = mimeType;
    }

    /**
     * <p>Serialize the object from the input stream provided.</p>
     *
     * @param ois the input stream to serialize the object from
     * @throws IOException thrown by
     * <code>ObjectInputStream.defaultReadObject()</code>.
     * @throws ClassNotFoundException thrown by
     * <code>ObjectInputStream.defaultReadObject()</code>.
     *
     * @roseuid 3E228C3701F7
     */
    private void readObject(final ObjectInputStream ois)
        throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    /**
     * <p>Serialize the object to the output stream provided.</p>
     *
     * @param oos the output stream to serialize the object to
     * @throws IOException thrown by
     * <code>ObjectOutputStream.defaultWriteObject()</code>
     *
     * @roseuid 3E228C3701F9
     */
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * <p>Get the content of the file.</p>
     *
     * @return content of the in a byte array object which can be passed
     * from
     * server-side to client-side (is serialized).
     *
     * @roseuid 3E228C3701FB
     */
    public final SerializedByteArray getContent() {
        return content;
    }

    /**
     * <p>Get the mime type of the file content.</p>
     *
     * @return clear-text mime type of the file content.
     *
     * @roseuid 3E228C3701FC
     */
    public final java.lang.String getMimeType() {
        return mimeType;
    }
}
