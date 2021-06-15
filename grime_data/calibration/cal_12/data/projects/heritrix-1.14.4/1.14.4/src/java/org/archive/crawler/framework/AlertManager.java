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
package org.archive.crawler.framework;

import java.util.Vector;

import org.archive.io.SinkHandlerLogRecord;


/**
 * Manager for application alerts.
 * An alert is a message to a human operator created by Heritrix when
 * exceptional conditions.
 * @author stack
 * @version $Date: 2006-09-25 23:59:43 +0000 (Mon, 25 Sep 2006) $ $Revision: 4664 $
 */
public interface AlertManager {
    /**
     * @param record The new alert to add.
     */
    public void add(final SinkHandlerLogRecord record);
    
    /**
     * @param alertID the ID of the alert to remove.
     */
    public void remove(final String alertID);

    /**
     * @param alertID The ID of the alert to return.
     * @return an alert with the given ID or null if none found.
     */
    public SinkHandlerLogRecord get(final String alertID);

    /**
     * @return All current alerts
     */
    public Vector getAll();

    /**
     * @return Vector of all new alerts.
     */
    public Vector getNewAll();

    /**
     * @return The number of alerts
     */
    public int getCount();

    /**
     * @return The number of new alerts
     */
    public int getNewCount();
    
    /**
     * @param alertID of the ID of the alert to mark as 'seen'.
     */
    public void read(final String alertID);
}
