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
package org.apache.james.imapserver;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.james.util.Assert;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;


/**
 * Implementation of a RecordRepository on a FileSystem.
 *
 * @version 0.2 on 04 Aug 2002
 * @see RecordRepository
 */
public class DefaultRecordRepository
    extends AbstractLogEnabled
    implements RecordRepository   {
 
    private String path;
    private File repository;

    /**
     * Returns the a unique UID validity value for this Host.
     * UID validity values are used to differentiate messages in 2 mailboxes with the same names
     * (when one is deleted).
     */
    public int nextUIDValidity()
    {
        // TODO - make this a better unique value
        // ( although this will probably never break in practice,
        //  should be incrementing a persisted value.
        return Math.abs( Calendar.getInstance().hashCode() );
    }

    /**
     * Deletes the FolderRecord from the repository.
     */
    public synchronized void deleteRecord( FolderRecord fr )
    {
        try {
            String key = path + File.separator + fr.getAbsoluteName();
            File record = new File( key );
            Assert.isTrue( Assert.ON &&
                           record.exists() );
            record.delete();
            getLogger().info("Record deleted for: " + fr.getAbsoluteName());
            notifyAll();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new
                RuntimeException("Exception caught while storing Folder Record: " + e);
        }
    }

    public void setPath(final String rootPath) {
        if (path != null) {
            throw new RuntimeException("Error: Attempt to reset AvalonRecordRepository");
        }
        path = rootPath;
        
        repository = new File(rootPath);

        if (!repository.isDirectory()) {
            if (! repository.mkdirs()){
                throw new RuntimeException("Error: Cannot create directory for AvalonRecordRepository at: " + rootPath);
            }
        } else if (!repository.canWrite()) {
            throw new RuntimeException("Error: Cannot write to directory for AvalonRecordRepository at: " + rootPath);
        }

                
    }

    public synchronized void store( final FolderRecord fr) {
        ObjectOutputStream out = null;
        try {
            String key = path + File.separator + fr.getAbsoluteName();
            out = new ObjectOutputStream( new FileOutputStream(key) );
            out.writeObject(fr);
            out.close();
            getLogger().info("Record stored for: " + fr.getAbsoluteName());
            notifyAll();
        } catch (Exception e) {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ignored) {
                }
            }
            e.printStackTrace();
            throw new
                RuntimeException("Exception caught while storing Folder Record: " + e);
        }
    }

    public synchronized Iterator getAbsoluteNames() {
        String[] names = repository.list();
        return Collections.unmodifiableList(Arrays.asList(names)).iterator();
    }

    public synchronized FolderRecord retrieve(final String folderAbsoluteName) {
        FolderRecord fr = null;
        ObjectInputStream in = null;
        try {
            String key = path + File.separator + folderAbsoluteName;
            in        = new ObjectInputStream( new FileInputStream(key) );
            fr = (FolderRecord) in.readObject();
            in.close();
  
        } catch (Exception e) {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ignored) {
                }
            }
            e.printStackTrace();
            throw new
                RuntimeException("Exception caught while reading Folder Record: " + e);
        } finally {
            notifyAll();
        }
        return fr;
    }
       
    public boolean containsRecord(String folderAbsoluteName) {
        File testFile = new File(repository, folderAbsoluteName);
        return testFile.exists();
    }
}

    
