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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.taskdefs.Expand;
import org.archive.net.UURI;

/**
 * Logging utils.
 * @author stack
 */
public class IoUtils {
    public static InputStream getInputStream(String pathOrUrl) {
        return getInputStream(null, pathOrUrl);
    }
    
    /**
     * Get inputstream.
     * 
     * This method looks at passed string and tries to judge it a
     * filesystem path or an URL.  It then gets an InputStream on to
     * the file or URL.
     * 
     * <p>ASSUMPTION: Scheme on any url will probably only ever be 'file' 
     * or 'http'.
     * 
     * @param basedir If passed <code>fileOrUrl</code> is a file path and
     * it is not absolute, prefix with this basedir (May be null then
     * no prefixing will be done).
     * @param pathOrUrl Pass path to a file on disk or pass in a URL.
     * @return An input stream.
     */
    public static InputStream getInputStream(File basedir, String pathOrUrl) {
        InputStream is = null;
        if (UURI.hasScheme(pathOrUrl)) {
            try {
                URL url = new URL(pathOrUrl);
                is = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Assume its not an URI or we failed the parse.
            // Try it as a file.
            File source = new File(pathOrUrl);
            if (!source.isAbsolute() && basedir != null) {
                source = new File(basedir, pathOrUrl);
            }
            try {
                is = new FileInputStream(source);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return is;
    }
    
    /**
     * Use ant to unjar.
     * @param zipFile File to unzip.
     * @param destinationDir Where to unzip to.
     */
    public static void unzip(File zipFile, File destinationDir) {
        unzip(zipFile, destinationDir, true);
    }
     
    /**
     * Use ant to unjar.
     * @param zipFile File to unzip.
     * @param destinationDir Where to unzip to.
     * @param overwrite Whether to overwrite existing content.
     */
    public static void unzip(File zipFile, File destinationDir,
            boolean overwrite) {
        final class Expander extends Expand {
                public Expander() {
            }   
        }
        Expander expander = new Expander();
        expander.setProject(new Project());
        expander.getProject().init();
        expander.setTaskType("unzip");
        expander.setTaskName("unzip");
        expander.setOwningTarget(new Target());
        expander.setSrc(zipFile);
        expander.setDest(destinationDir);
        expander.setOverwrite(overwrite);
        expander.execute();
    }
}
