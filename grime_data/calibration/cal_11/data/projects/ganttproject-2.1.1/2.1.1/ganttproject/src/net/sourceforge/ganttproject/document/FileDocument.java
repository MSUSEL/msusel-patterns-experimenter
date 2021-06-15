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
/*
 * Created on 18.08.2003
 *
 */
package net.sourceforge.ganttproject.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * This class implements the interface Document for file access on local file
 * systems.
 * 
 * @author Michael Haeusler (michael at akatose.de)
 */
public class FileDocument extends AbstractDocument {

    private File file;

    public FileDocument(File file) {
        this.file = file;
    }

    public String getDescription() {
        return file.getName();
    }

    public boolean canRead() {
        return file.canRead();
    }

    public boolean canWrite() {
        boolean writable = file.canWrite();
        if (!writable && !file.exists()) {
            try {
                if (file.createNewFile()) {
                    file.delete();
                    writable = true;
                }
            } catch (IOException e) {
            }
        }
        return (writable);
    }

    public boolean isValidForMRU() {
        return file.exists();
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public OutputStream getOutputStream() throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    public String getPath() {
        return file.getPath();
    }

    public String getFilePath() {
        return getPath();
    }

    public void open() throws IOException {

    }

    public void write() throws IOException {
        // TODO Auto-generated method stub
        
    }

    public URI getURI() {
        return file.toURI();
    }

    public boolean isLocal() {
        return true;
    }
    
    
}
