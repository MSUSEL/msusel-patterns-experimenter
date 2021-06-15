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

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Class to display version information about HtmlUnit. This is the class
 * that will be executed if the JAR file is run.
 *
 * @version $Revision: 5918 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
public final class Version {

    /** Prevent instantiation. */
    private Version() {
        // Empty.
    }

    /**
     * The main entry point into this class.
     * @param args the arguments passed on the command line
     * @throws Exception if an error occurs
     */
    public static void main(final String[] args) throws Exception {
        if (args.length == 1 && args[0].equals("-SanityCheck")) {
            runSanityCheck();
            return;
        }
        System.out.println(getProductName());
        System.out.println(getCopyright());
        System.out.println("Version: " + getProductVersion());
    }

    /**
     * Runs the sanity check.
     * @throws Exception if anything goes wrong
     */
    private static void runSanityCheck() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage("http://htmlunit.sourceforge.net/index.html");
        page.executeJavaScript("document.location");
        System.out.println("Sanity check complete.");
    }

    /**
     * Returns "HtmlUnit".
     * @return "HtmlUnit"
     */
    public static String getProductName() {
        return "HtmlUnit";
    }

    /**
     * Returns the current implementation version.
     * @return the current implementation version
     */
    public static String getProductVersion() {
        return Version.class.getPackage().getImplementationVersion();
    }

    /**
     * Returns the copyright notice.
     * @return the copyright notice
     */
    public static String getCopyright() {
        return "Copyright (c) 2002-2010 Gargoyle Software Inc. All rights reserved.";
    }
}
