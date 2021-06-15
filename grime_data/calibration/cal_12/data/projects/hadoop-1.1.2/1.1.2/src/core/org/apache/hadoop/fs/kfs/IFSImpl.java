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
 * We need to provide the ability to the code in fs/kfs without really
 * having a KFS deployment.  In particular, the glue code that wraps
 * around calls to KfsAccess object.  This is accomplished by defining a
 * filesystem implementation interface:  
 *   -- for testing purposes, a dummy implementation of this interface
 * will suffice; as long as the dummy implementation is close enough
 * to doing what KFS does, we are good.
 *   -- for deployment purposes with KFS, this interface is
 * implemented by the KfsImpl object.
 */

package org.apache.hadoop.fs.kfs;

import java.io.*;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;

interface IFSImpl {
    public boolean exists(String path) throws IOException;
    public boolean isDirectory(String path) throws IOException;
    public boolean isFile(String path) throws IOException;
    public String[] readdir(String path) throws IOException;
    public FileStatus[] readdirplus(Path path) throws IOException;

    public int mkdirs(String path) throws IOException;
    public int rename(String source, String dest) throws IOException;

    public int rmdir(String path) throws IOException; 
    public int remove(String path) throws IOException;
    public long filesize(String path) throws IOException;
    public short getReplication(String path) throws IOException;
    public short setReplication(String path, short replication) throws IOException;
    public String[][] getDataLocation(String path, long start, long len) throws IOException;

    public long getModificationTime(String path) throws IOException;
    public FSDataOutputStream create(String path, short replication, int bufferSize) throws IOException;
    public FSDataInputStream open(String path, int bufferSize) throws IOException;
    
};
