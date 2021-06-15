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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

/**
 * An abstract page that represents some content returned from a server.
 *
 * @version $Revision: 5764 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Marc Guillemot
 */
public interface Page extends Serializable {

    /**
     * Initialize this page.
     * This method gets called when a new page is loaded and you should probably never
     * need to call it directly.
     * @throws IOException if an IO problem occurs
     */
    void initialize() throws IOException;

    /**
     * Clean up this page.
     * This method gets called by the web client when an other page is loaded in the window
     * and you should probably never need to call it directly
     * @throws IOException if an IO problem occurs
     */
    void cleanUp() throws IOException;

    /**
     * Returns the web response that was originally used to create this page.
     * @return the web response
     */
    WebResponse getWebResponse();

    /**
     * Returns the window that this page is sitting inside.
     * @return the enclosing window
     */
    WebWindow getEnclosingWindow();

    /**
     * Returns the URL of this page.
     * @return the URL of this page
     */
    URL getUrl();
}
