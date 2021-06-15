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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.swing.testutils;

import java.awt.AWTEvent;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.WindowFixture;

/**
 * Listens for a window of specified class to be activated on the AWT event thread
 * and, when it appears, creates a FEST {@linkplain WindowFixture} object from it.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class WindowActivatedListener implements AWTEventListener {

    private final Class<? extends Window> windowClass;
    private final CountDownLatch latch;
    private WindowFixture fixture;

    /**
     * Creates a new listener.
     * 
     * @param windowClass the class to listen for.
     */
    public WindowActivatedListener(Class<? extends Window> windowClass) {
        if (Frame.class.isAssignableFrom(windowClass)
                || Dialog.class.isAssignableFrom(windowClass)) {
            this.windowClass = windowClass;

        } else {
            throw new UnsupportedOperationException(
                    windowClass.getName() + " is not supported");
        }

        this.latch = new CountDownLatch(1);
    }

    /**
     * Checks if an event pertains to this listener's target window class
     * and is of type {@linkplain WindowEvent#WINDOW_ACTIVATED}.
     * 
     * @param event an event
     */
    @Override
    public void eventDispatched(AWTEvent event) {
        if (fixture == null) {
            Object source = event.getSource();
            if (windowClass.isAssignableFrom(source.getClass())
                    && event.getID() == WindowEvent.WINDOW_ACTIVATED) {

                if (source instanceof Frame) {
                    fixture = new FrameFixture((Frame) source);
                } else if (source instanceof Dialog) {
                    fixture = new DialogFixture((Dialog) source);
                }

                latch.countDown();
            }
        }
    }

    /**
     * Gets the {@linkplain WindowFixture} created by this listener if available.
     * 
     * @param timeOutMillis maximum waiting time in milliseconds
     * @return the fixture or {@code null} if the time-out expires
     * 
     * @throws InterruptedException on interruption while waiting for the fixture to
     *     become available
     */
    public WindowFixture getFixture(long timeOutMillis) throws InterruptedException {
        if (latch.await(timeOutMillis, TimeUnit.MILLISECONDS)) {
            return fixture;
        } else {
            return null;
        }
    }
}
