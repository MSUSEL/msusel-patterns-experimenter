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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * Test suite for {@link SubProgressListener}
 * 
 * @author groldan
 * @version $Id$
 *
 *
 * @source $URL$
 */
public class SubProgressListenerTest {

    @Test
    public void testSubProgressStartComplete() {
        SimpleProgressListener parent = new SimpleProgressListener();
        SubProgressListener sub = new SubProgressListener(parent, 50);

        sub.started();
        assertEquals(0F, sub.getProgress());
        sub.complete();
        assertEquals(100F, sub.getProgress());
    }

    @Test
    public void testSubProgressBounds() {
        SimpleProgressListener parent = new SimpleProgressListener();

        parent.progress(50f);

        SubProgressListener sub = new SubProgressListener(parent, 50);

        sub.started();
        sub.progress(50f);

        assertEquals(50f, sub.getProgress());
        assertEquals(75f, parent.getProgress());

        sub.progress(100f);
        
        assertEquals(100f, sub.getProgress());
        assertEquals(100f, parent.getProgress());
    }

    private static class SimpleProgressListener implements ProgressListener {

        private float progress;

        public void progress(float percent) {
            this.progress = percent;
        }

        public float getProgress() {
            return this.progress;
        }

        public void complete() {
        }

        public void started() {
        }

        public void dispose() {
        }

        public void exceptionOccurred(Throwable exception) {
        }

        public String getDescription() {
            return null;
        }

        public InternationalString getTask() {
            return null;
        }

        public boolean isCanceled() {
            return false;
        }

        public void setCanceled(boolean cancel) {
        }

        public void setDescription(String description) {
        }

        public void setTask(InternationalString task) {
        }

        public void warningOccurred(String source, String location, String warning) {
        }
    }
}
