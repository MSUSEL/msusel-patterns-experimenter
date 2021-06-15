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
package org.archive.crawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Utilities useful checkpointing.
 * @author stack
 * @version $Date: 2006-09-25 22:40:19 +0000 (Mon, 25 Sep 2006) $ $Revision: 4658 $
 */
public class CheckpointUtils {
    public static final String SERIALIZED_CLASS_SUFFIX = ".serialized";
    
    public static File getBdbSubDirectory(File checkpointDir) {
        return new File(checkpointDir, "bdbje-logs");
    }
    
    public static File getClassCheckpointFile(File checkpointDir,
            final String suffix, Class c) {
        return new File(checkpointDir, getClassCheckpointFilename(c, suffix));
    }
    
    public static File getClassCheckpointFile(File checkpointDir, Class c) {
        return new File(checkpointDir, getClassCheckpointFilename(c, null));
    }
    
    public static String getClassCheckpointFilename(final Class c) {
        return getClassCheckpointFilename(c, null);
    }
    
    public static String getClassCheckpointFilename(final Class c,
            final String suffix) {
        return c.getName() + ((suffix == null)? "": "." + suffix) +
            SERIALIZED_CLASS_SUFFIX;
    }
    
    /**
     * Utility function to serialize an object to a file in current checkpoint
     * dir. Facilities
     * to store related files alongside the serialized object in a directory
     * named with a <code>.auxillary</code> suffix.
     *
     * @param o Object to serialize.
     * @param dir Directory to serialize into.
     * @throws IOException
     */
    public static void writeObjectToFile(final Object o, final File dir)
    throws IOException {
        writeObjectToFile(o, null, dir);
    }
        
    public static void writeObjectToFile(final Object o, final String suffix,
            final File dir)
    throws IOException {
        dir.mkdirs();
        ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream(getClassCheckpointFile(dir, suffix,
                o.getClass())));
        try {
            out.writeObject(o);
        } finally {
            out.close();
        }
    }
    
    public static <T> T readObjectFromFile(final Class<T> c, final File dir)
    throws FileNotFoundException, IOException, ClassNotFoundException {
        return readObjectFromFile(c, null, dir);
    }
    
    public static <T> T readObjectFromFile(final Class<T> c, final String suffix,
            final File dir)
    throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(getClassCheckpointFile(dir, suffix, c)));
        T o = null;
        try {
            o = c.cast(in.readObject());
        } finally {
            in.close();
        }
        return o;
    }

    /**
     * @return Instance of filename filter that will let through files ending
     * in '.jdb' (i.e. bdb je log files).
     */
    public static FilenameFilter getJeLogsFilter() {
        return new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name != null && name.toLowerCase().endsWith(".jdb");
            }
        };
    }
}
