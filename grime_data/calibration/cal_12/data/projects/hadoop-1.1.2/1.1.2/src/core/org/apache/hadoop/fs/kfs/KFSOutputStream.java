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
/**
 *
 * Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * @author: Sriram Rao (Kosmix Corp.)
 * 
 * Implements the Hadoop FSOutputStream interfaces to allow applications to write to
 * files in Kosmos File System (KFS).
 */

package org.apache.hadoop.fs.kfs;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.ByteBuffer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.util.Progressable;

import org.kosmix.kosmosfs.access.KfsAccess;
import org.kosmix.kosmosfs.access.KfsOutputChannel;

class KFSOutputStream extends OutputStream {

    private String path;
    private KfsOutputChannel kfsChannel;

    public KFSOutputStream(KfsAccess kfsAccess, String path, short replication) {
        this.path = path;

        this.kfsChannel = kfsAccess.kfs_create(path, replication);
    }

    public long getPos() throws IOException {
        if (kfsChannel == null) {
            throw new IOException("File closed");
        }
        return kfsChannel.tell();
    }

    public void write(int v) throws IOException {
        if (kfsChannel == null) {
            throw new IOException("File closed");
        }
        byte[] b = new byte[1];

        b[0] = (byte) v;
        write(b, 0, 1);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (kfsChannel == null) {
            throw new IOException("File closed");
        }

        kfsChannel.write(ByteBuffer.wrap(b, off, len));
    }

    public void flush() throws IOException {
        if (kfsChannel == null) {
            throw new IOException("File closed");
        }
        kfsChannel.sync();
    }

    public synchronized void close() throws IOException {
        if (kfsChannel == null) {
            return;
        }
        flush();
        kfsChannel.close();
        kfsChannel = null;
    }
}
