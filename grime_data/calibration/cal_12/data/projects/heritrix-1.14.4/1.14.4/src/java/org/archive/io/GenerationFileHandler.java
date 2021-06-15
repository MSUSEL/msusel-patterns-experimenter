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
/* GenerationFileHandler
*
* $Id: GenerationFileHandler.java 4646 2006-09-22 17:23:04Z paul_jack $
*
* Created on May 18, 2004
*
* Copyright (C) 2004 Internet Archive.
*
* This file is part of the Heritrix web crawler (crawler.archive.org).
*
* Heritrix is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser Public License as published by
* the Free Software Foundation; either version 2.1 of the License, or
* any later version.
*
* Heritrix is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser Public License for more details.
*
* You should have received a copy of the GNU Lesser Public License
* along with Heritrix; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.archive.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;


/**
 * FileHandler with support for rotating the current file to
 * an archival name with a specified integer suffix, and
 * provision of a new replacement FileHandler with the current
 * filename.
 *
 * @author gojomo
 */
public class GenerationFileHandler extends FileHandler {
    private LinkedList<String> filenameSeries = new LinkedList<String>();
    private boolean shouldManifest = false;

    /**
     * @return Returns the filenameSeries.
     */
    public List getFilenameSeries() {
        return filenameSeries;
    }

    /**
     * Constructor.
     * @param pattern
     * @param append
     * @param shouldManifest
     * @throws IOException
     * @throws SecurityException
     */
    public GenerationFileHandler(String pattern, boolean append,
            boolean shouldManifest)
    throws IOException, SecurityException {
        super(pattern, append);
        filenameSeries.addFirst(pattern);
        this.shouldManifest = shouldManifest;
    }

    /**
     * @param filenameSeries
     * @param shouldManifest
     * @throws IOException
     */
    public GenerationFileHandler(LinkedList<String> filenameSeries,
            boolean shouldManifest)
    throws IOException {
        super((String)filenameSeries.getFirst(), false); // Never append in this case
        this.filenameSeries = filenameSeries;
        this.shouldManifest = shouldManifest;
    }

    /**
     * Move the current file to a new filename with the storeSuffix in place
     * of the activeSuffix; continuing logging to a new file under the
     * original filename.
     *
     * @param storeSuffix Suffix to put in place of <code>activeSuffix</code>
     * @param activeSuffix Suffix to replace with <code>storeSuffix</code>.
     * @return GenerationFileHandler instance.
     * @throws IOException
     */
    public GenerationFileHandler rotate(String storeSuffix,
            String activeSuffix)
    throws IOException {
        close();
        String filename = (String)filenameSeries.getFirst();
        if (!filename.endsWith(activeSuffix)) {
            throw new FileNotFoundException("Active file does not have" +
                " expected suffix");
        }
        String storeFilename = filename.substring(0,
             filename.length() - activeSuffix.length()) +
             storeSuffix;
        File activeFile = new File(filename);
        File storeFile = new File(storeFilename);
        if (!activeFile.renameTo(storeFile)) {
            throw new IOException("Unable to move " + filename + " to " +
                storeFilename);
        }
        filenameSeries.add(1, storeFilename);
        GenerationFileHandler newGfh = 
            new GenerationFileHandler(filenameSeries, shouldManifest);
        newGfh.setFormatter(this.getFormatter());
        return newGfh;
    }
    
    /**
     * @return True if should manifest.
     */
    public boolean shouldManifest() {
        return this.shouldManifest;
    }
}