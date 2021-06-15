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
package com.gargoylesoftware.htmlunit.source;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Subversion utilities.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public final class SVN {

    private SVN() { }

    /**
     * Recursively deletes any '.svn' folder which contains Subversion information.
     * @param dir the directory to recursively delete '.svn' from
     * @throws IOException if an exception happens
     */
    public static void deleteSVN(final File dir) throws IOException {
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                if (f.getName().equals(".svn")) {
                    FileUtils.deleteDirectory(f);
                }
                else {
                    deleteSVN(f);
                }
            }
        }
    }

    /**
     * Ensures that all files inside the specified directory has consistent new lines.
     * @param dir the directory to recursively ensure all contained files have consistent new lines
     * @throws IOException if an exception happens
     */
    public static void consistentNewlines(final File dir) throws IOException {
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                if (!f.getName().equals(".svn")) {
                    consistentNewlines(f);
                }
            }
            else {
                FileUtils.writeLines(f, FileUtils.readLines(f));
            }
        }
    }
}
