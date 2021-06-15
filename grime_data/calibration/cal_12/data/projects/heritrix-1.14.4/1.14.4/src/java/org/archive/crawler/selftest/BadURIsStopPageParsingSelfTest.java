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
package org.archive.crawler.selftest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Selftest for figuring problems parsing URIs in a page.
 * 
 * @author stack
 * @see <a 
 * href="https://sourceforge.net/tracker/?func=detail&aid=788219&group_id=73833&atid=539099">[ 788219 ]
 * URI Syntax Errors stop page parsing.</a>
 * @version $Revision: 4931 $, $Date: 2007-02-21 18:48:17 +0000 (Wed, 21 Feb 2007) $
 */
public class BadURIsStopPageParsingSelfTest extends SelfTestCase
{
    /**
     * Files to find as a list.
     * 
     * We don't find goodtwo.html because it has a BASE that is out
     * of scope.
     */
    private static final List<File> FILES_TO_FIND =
        Arrays.asList(new File[]
            {new File("goodone.html"),
                new File("goodthree.html"),
                new File("one.html"),
                new File("two.html"),
                new File("three.html")});

    public void stestFilesFound() {
        assertInitialized();
        List<File> foundFiles = filesFoundInArc();
        ArrayList<File> editedFoundFiles
         = new ArrayList<File>(foundFiles.size());
        for (Iterator i = foundFiles.iterator(); i.hasNext();) {
            File f = (File)i.next();
            if (f.getAbsolutePath().endsWith("polishex.html")) {
                // There is a URI in our list with the above as suffix.  Its in
                // the arc as a 404. Remove it.  It doesn't exist on disk so it
                // will cause the below testFilesInArc to fail.
                continue;
            }
            editedFoundFiles.add(f);
        }
        testFilesInArc(FILES_TO_FIND, editedFoundFiles);
    }
}
