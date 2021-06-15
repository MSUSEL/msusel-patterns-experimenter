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
package org.archive.crawler.settings.refinements;

import org.archive.net.UURI;


/**
 * A refinement criterion that checks if a URI matches a specific port number.
 * <p/>
 * If the port number is not known it will try to use the default port number
 * for the URI's scheme.
 *
 * @author John Erik Halse
 */
public class PortnumberCriteria implements Criteria {
    private int portNumber = 0;

    /**
     * Create a new instance of PortnumberCriteria.
     */
    public PortnumberCriteria() {
        super();
    }

    /**
     * Create a new instance of PortnumberCriteria.
     *
     * @param portNumber the port number for this criteria.
     */
    public PortnumberCriteria(String portNumber) {
        setPortNumber(portNumber);
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.refinements.Criteria#isWithinRefinementBounds(org.archive.crawler.datamodel.UURI, int)
     */
    public boolean isWithinRefinementBounds(UURI uri) {
        int port = uri.getPort();
        if (port < 0) {
            if (uri.getScheme().equals("http")) {
                port = 80;
            } else if (uri.getScheme().equals("https")) {
                port = 443;
            }
        }

        return (port == portNumber)? true: false;
    }

    /**
     * Get the port number that is to be checked against a URI.
     *
     * @return Returns the portNumber.
     */
    public String getPortNumber() {
        return String.valueOf(portNumber);
    }
    /**
     * Set the port number that is to be checked against a URI.
     *
     * @param portNumber The portNumber to set.
     */
    public void setPortNumber(String portNumber) {
        this.portNumber = Integer.parseInt(portNumber);
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.refinements.Criteria#getName()
     */
    public String getName() {
        return "Port number criteria";
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.refinements.Criteria#getDescription()
     */
    public String getDescription() {
        return "Accept URIs on port " + getPortNumber();
    }
}
