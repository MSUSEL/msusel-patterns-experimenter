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

import java.util.EventObject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An event listener that can be set to expect specified types of events
 * and test if they are received.
 * 
 * @param <E> the Enum type associated with the event class
 * @param <T> type of {@code EventObject}
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class WaitingListener<T extends EventObject, E extends Enum> {
    protected final int NTYPES;

    protected CountDownLatch[] latches;
    protected AtomicBoolean[] flags;
    protected EventObject[] events;
    
    public WaitingListener(int numEventTypes) {
        NTYPES = numEventTypes;
        latches = new CountDownLatch[NTYPES];
        events = new EventObject[NTYPES];
        
        flags = new AtomicBoolean[NTYPES];
        for (int i = 0; i < NTYPES; i++) {
            flags[i] = new AtomicBoolean(false);
        }
    }
    
    /**
     * Sets the listener to expect one event of the specified type.
     * 
     * @param type event type
     */
    public synchronized void setExpected(E type) {
        setExpected(type, 1);
    }
    
    /**
     * Sets the listener to expect {@code count} events of the specified type.
     * 
     * @param type event type
     * @param count number of events
     */
    public synchronized void setExpected(E type, int count) {
        latches[type.ordinal()] = new CountDownLatch(count);
    }
    
    /**
     * Waits of an event of the specified type to be received.
     * 
     * @param type event type
     * @param timeoutMillis maximum waiting time
     * 
     * @return {@code true} if the event was received
     */
    public synchronized boolean await(E type, long timeoutMillis) {
        CountDownLatch latch = latches[type.ordinal()];
        if (latch == null) {
            throw new IllegalStateException("latch not set for " + type);
        }
        
        boolean result = false;
        try {
            result = latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            // do nothing
        } finally {
            latches[type.ordinal()] = null;
            return result;
        }
    }

    /**
     * Checks if an event of the specified type has been received.
     * 
     * @param type event type
     * 
     * @return {@code true} if an event of this type has been received
     */
    public synchronized boolean eventReceived(E type) { // MapPaneEvent.Type type) {
        return flags[type.ordinal()].get();
    }

    /**
     * Retrieves the more recent event of the specified type received
     * by this listener.
     * 
     * @param type event type
     * 
     * @return the most recent event or {@code null} if none received
     */
    public T getEvent(E type) {
        return (T) events[type.ordinal()];
    }

    protected void catchEvent(int k, T event) {
        flags[k].set(true);
        events[k] = event;
        
        CountDownLatch latch = latches[k];
        if (latch != null) {
            latch.countDown();
        }
        
    }

}
