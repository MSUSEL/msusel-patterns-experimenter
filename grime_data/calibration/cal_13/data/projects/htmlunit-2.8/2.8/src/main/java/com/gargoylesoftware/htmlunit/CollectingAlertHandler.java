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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple alert handler that keeps track of alerts in a list.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class CollectingAlertHandler implements AlertHandler, Serializable {

    private static final long serialVersionUID = 6617913244195961483L;

    private final List<String> collectedAlerts_;

    /**
     * Creates a new instance, initializing it with an empty list.
     */
    public CollectingAlertHandler() {
        this(new ArrayList<String>());
    }

    /**
     * Creates an instance with the specified list.
     *
     * @param list the list to store alerts in
     */
    public CollectingAlertHandler(final List<String> list) {
        WebAssert.notNull("list", list);
        collectedAlerts_ = list;
    }

    /**
     * Handles the alert. This implementation will store the message in a list
     * for retrieval later.
     *
     * @param page the page that triggered the alert
     * @param message the message in the alert
     */
    public void handleAlert(final Page page, final String message) {
        collectedAlerts_.add(message);
    }

    /**
     * Returns a list containing the message portion of any collected alerts.
     * @return a list of alert messages
     */
    public List<String> getCollectedAlerts() {
        return collectedAlerts_;
    }
}
