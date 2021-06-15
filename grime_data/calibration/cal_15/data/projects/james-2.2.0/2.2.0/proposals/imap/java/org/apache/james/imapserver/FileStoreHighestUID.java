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

import java.io.*;
import java.util.*;

/**
 * Reference: RFC 2060
 * @version 0.1 on 15 Aug 2002
 */
public class FileStoreHighestUID implements HighestUID {

    private int highestUID;
    private int whenToWrite;
    private static final int WRITE_STEP = 3;
    private File file;
  
    public FileStoreHighestUID(File f) {
        file = f;
        highestUID = 0;
    
        if (file.exists()) {
            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(new FileInputStream(file));
                Integer i = (Integer) is.readObject();
                highestUID = i.intValue();
                is.close();
                is = null;
            } catch (Exception ex) {
                // log here
                ex.printStackTrace();
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception ignored) {}
                }
                throw new RuntimeException("Could not load highestUID!");
            }
            // maybe james was stopped without writing correct highestUID, therefore add
            // STEP_HIGHEST_UID, to ensure uniqeness of highestUID.
            highestUID += WRITE_STEP;
        }
        write();
        whenToWrite = highestUID+WRITE_STEP;
        System.out.println("Initialized highestUID="+highestUID);
    }
  
    public synchronized int get() {
        return highestUID;
    }
  
    public synchronized void increase() {
        highestUID++;
        if (highestUID >= whenToWrite) {
            // save this highestUID
            whenToWrite = highestUID+WRITE_STEP;
            // make it persistent
            write();
        }
    }
  
    private void write() {
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream( new FileOutputStream(file));
            os.writeObject(new Integer(highestUID));
            os.close();
            os = null;
        } catch (Exception ex) {
            // log here
            ex.printStackTrace();
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ignored) {}
            }
            throw new RuntimeException("Failed to save highestUID!");
        }
    }
}
