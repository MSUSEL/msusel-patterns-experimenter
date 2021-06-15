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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Patches utilities.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public final class Patch {

    private Patch() { }

    /**
     * Checks the @author tag in the files touched by the specified patch.
     *
     * @param baseDir the root folder of HtmlUnit, this can be '.' if you are calling this methods from HtmlUnit code
     * base. If you are calling this method from another project, this specifies HtmlUnit home folder.
     * @param patchPath the path to the patch
     * @param authorName the author name, e.g. "John Smith"
     * @throws Exception if an exception occurs
     */
    @SuppressWarnings("unchecked")
    public static void checkAuthor(final String baseDir, final String patchPath, final String authorName)
        throws Exception {
        final List<String> errors = new ArrayList<String>();
        final List<String> lines = FileUtils.readLines(new File(patchPath));
        for (final String line : lines) {
            if (line.startsWith("+++")) {
                final String fileName = line.substring(4, line.indexOf('\t', 4));
                if (fileName.endsWith(".java")) {
                    final File file = new File(baseDir, fileName);
                    if (file.exists()) {
                        final List<String> fileLines = FileUtils.readLines(file);
                        boolean authorFound = false;
                        for (final String fileLine : fileLines) {
                            if (fileLine.contains("@author " + authorName)) {
                                authorFound = true;
                                break;
                            }
                        }
                        if (!authorFound) {
                            System.out.println("No \"@author " + authorName + "\" in " + file.getAbsolutePath());
                            errors.add(file.getAbsolutePath());
                        }
                    }
                    else {
                        System.out.println("File does not exist: " + file);
                    }
                }
            }
        }
        if (!errors.isEmpty()) {
            throw new Exception("Total missing files: " + errors.size());
        }
    }

}
