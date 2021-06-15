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
package org.apache.james.context;

import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;

import java.io.File;
import java.io.IOException;

/**
 * This class is essentially a set of static functions for
 * extracting information from the Avalon context.  This class
 * should never be instantiated.  Each function takes the context
 * as a first argument.
 */
public class AvalonContextUtilities {

    /**
     * The file URL prefix
     */
    private static String filePrefix = "file://";

    /**
     * The file URL prefix length
     */
    private static int filePrefixLength = filePrefix.length();

    /**
     * Gets the file or directory described by the argument file URL.
     *
     * @param context the Avalon context
     * @param fileURL an appropriately formatted URL describing a file on
     *                the filesystem on which the server is running.  The 
     *                URL is evaluated as a location relative to the
     *                application home, unless it begins with a slash.  In
     *                the latter case the file location is evaluated relative
     *                to the underlying file system root.
     *
     * @throws IllegalArgumentException if the arguments are null or the file
     *                                  URL is not appropriately formatted.
     * @throws ContextException if the underlying context generates a
     *                          ContextException, if the application home is
     *                          not correct, or if an IOException is generated
     *                          while accessing the file.
     */
    public static File getFile(Context context, String fileURL)
            throws Exception {
        if ((context == null) || (fileURL == null)) {
            throw new IllegalArgumentException("The getFile method doesn't allow null arguments.");
        }
        String internalFileURL = fileURL.trim();
        if (!(internalFileURL.startsWith(filePrefix))) {
            throw new IllegalArgumentException("The fileURL argument to getFile doesn't start with the required file prefix - "  + filePrefix);
        }

        String fileName = fileURL.substring(filePrefixLength);
        if (!(fileName.startsWith("/"))) {
            String baseDirectory = "";
            try {
                File applicationHome =
                    (File)context.get(AvalonContextConstants.APPLICATION_HOME);
                baseDirectory = applicationHome.toString();
            } catch (ContextException ce) {
                throw new ContextException("Encountered exception when resolving application home in Avalon context.", ce);
            } catch (ClassCastException cce) {
                throw new ContextException("Application home object stored in Avalon context was not of type java.io.File.", cce);
            }
            StringBuffer fileNameBuffer =
                new StringBuffer(128)
                    .append(baseDirectory)
                    .append(File.separator)
                            .append(fileName);
            fileName = fileNameBuffer.toString();
        }
        try {
            File returnValue = (new File(fileName)).getCanonicalFile();
            return returnValue;
        } catch (IOException ioe) {
            throw new ContextException("Encountered an unexpected exception while retrieving file.", ioe);
        }
    }

    /**
     * Private constructor to ensure that instances of this class aren't
     * instantiated.
     */
    private AvalonContextUtilities() {}
}
