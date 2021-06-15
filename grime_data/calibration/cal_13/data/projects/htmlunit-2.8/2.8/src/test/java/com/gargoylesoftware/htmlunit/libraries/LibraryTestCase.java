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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Common functionalities for library tests.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
public abstract class LibraryTestCase extends WebTestCase {

    /**
     * Loads an expectation file for the given test in the library folder.
     * TODO: use browser version specific variations too
     * @param testName the base name for the file
     * @return the content of the file
     * @throws Exception in case of error
     */
    protected String loadExpectation(final String testName) throws Exception {
        // TODO: check first if a file specific to current browserVersion exists first with version number then without
        final String resource = getLibraryDir() + "/" + testName + ".expected.txt";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);
        final File file = new File(url.toURI());

        return FileUtils.readFileToString(file, "UTF-8");
    }

    /**
     * Gets the base folder containing the files for this JS library.
     * @return the path without "/" at the end
     */
    protected abstract String getLibraryDir();

}
