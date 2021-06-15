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
 *
 */
package org.geotools.arcsde.data;

import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * An implementation of ProgressListener to use when testing.
 * <p>
 * This implementation is good about throwing illegal state exceptions and so forth.
 * 
 * @author Jody
 * 
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/test/java/org
 *         /geotools/arcsde/data/TestProgressListener.java $
 */
public class TestProgressListener implements ProgressListener {
    int progressCount;

    float progress;

    int taskCount;

    InternationalString task;

    boolean isCanceled;

    int exceptionCount;

    Throwable exception;

    boolean completed;

    private boolean started;

    @SuppressWarnings("unused")
    private String[] warning;

    public void reset() {
        progressCount = 0;
        progress = 0f;
        taskCount = 0;
        task = null;
        isCanceled = false;
        exceptionCount = 0;
        exception = null;
        completed = false;
        started = false;
        warning = null;
    }

    public void complete() {
        if (completed)
            throw new IllegalStateException("Cannot complete twice");
        progress = 100f;
        completed = true;
    }

    public void dispose() {
        reset();
    }

    public void exceptionOccurred(Throwable exception) {
        this.exception = exception;
    }

    public String getDescription() {
        return task == null ? null : task.toString();
    }

    public float getProgress() {
        return progress;
    }

    public InternationalString getTask() {
        return task;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void progress(float percent) {
        if (!started)
            throw new IllegalStateException("Cannot record progress unless started");
        if (completed)
            throw new IllegalStateException("Cannot record progress when completed");
        progress = percent;
    }

    public void setCanceled(boolean cancel) {
        if (!started)
            throw new IllegalStateException("Cannot canel unless started");
        if (completed)
            throw new IllegalStateException("Cannot cancel when completed");
        isCanceled = cancel;
    }

    public void setDescription(String description) {
        task = new SimpleInternationalString(description);
    }

    public void setTask(InternationalString task) {
        this.task = task;
    }

    public void started() {
        if (started)
            throw new IllegalStateException("Cannot start twice");
        this.started = true;
    }

    public void warningOccurred(String source, String location, String warning) {
        this.warning = new String[] { source, location, warning };
    }

}
