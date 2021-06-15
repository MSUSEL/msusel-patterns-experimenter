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
package org.apache.james.mailrepository;

import org.apache.avalon.cornerstone.services.store.StreamRepository;
import org.apache.james.core.MimeMessageSource;

import java.io.IOException;
import java.io.InputStream;

public class MimeMessageAvalonSource extends MimeMessageSource {

    //Define how to get to the data
    
    /**
     * The stream repository used by this data source.
     */
    StreamRepository sr = null;

    /**
     * The name of the repository
     */
    String repositoryName = null;

    /**
     * The key for the particular stream in the stream repository
     * to be used by this data source.
     */
    String key = null;

    private long size = -1;

    public MimeMessageAvalonSource(StreamRepository sr, String repositoryName, String key) {
        this.sr = sr;
        this.repositoryName = repositoryName;
        this.key = key;
    }

    /**
     * Returns a unique String ID that represents the location from where 
     * this source is loaded.  This will be used to identify where the data 
     * is, primarily to avoid situations where this data would get overwritten.
     *
     * @return the String ID
     */
    public String getSourceId() {
        StringBuffer sourceIdBuffer =
            new StringBuffer(128)
                    .append(repositoryName)
                    .append("/")
                    .append(key);
        return sourceIdBuffer.toString();
    }

    public InputStream getInputStream() throws IOException {
        return sr.get(key);
    }

    public long getMessageSize() throws IOException {
        if (size == -1) {
            if (sr instanceof org.apache.james.mailrepository.filepair.File_Persistent_Stream_Repository) {
                size = ((org.apache.james.mailrepository.filepair.File_Persistent_Stream_Repository) sr).getSize(key);
            } else size = super.getMessageSize();
        }
        return size;
    }
}
