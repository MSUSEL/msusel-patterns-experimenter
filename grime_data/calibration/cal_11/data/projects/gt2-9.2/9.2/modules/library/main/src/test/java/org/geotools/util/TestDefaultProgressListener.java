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
/**
 * 
 */
package org.geotools.util;

import junit.framework.Assert;

import org.junit.Test;


/**
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 *
 *
 * @source $URL$
 */
public class TestDefaultProgressListener extends Assert {

    @Test
    public void testA(){
        final DefaultProgressListener listener = new DefaultProgressListener();
        listener.setTask(SimpleInternationalString.wrap("task"));
        assertNotNull(listener.getTask());
        assertTrue(listener.getTask().toString().equalsIgnoreCase("task"));
        
        listener.complete();
        assertTrue(listener.isCompleted());
        assertFalse(listener.isCanceled());
        assertFalse(listener.isStarted());
        
        listener.setCanceled(true);
        assertTrue(listener.isCompleted());
        assertTrue(listener.isCanceled());
        assertFalse(listener.isStarted());
        
        listener.complete();
        assertTrue(listener.isCompleted());
        assertTrue(listener.isCanceled());
        assertFalse(listener.isStarted());
        
        listener.started();
        assertTrue(listener.isCompleted());
        assertTrue(listener.isCanceled());
        assertTrue(listener.isStarted());
        
        listener.exceptionOccurred(new RuntimeException("error"));
        assertTrue(listener.hasExceptions());
        assertTrue(listener.getExceptions().peek() instanceof RuntimeException);
        
        
        
    }
}
