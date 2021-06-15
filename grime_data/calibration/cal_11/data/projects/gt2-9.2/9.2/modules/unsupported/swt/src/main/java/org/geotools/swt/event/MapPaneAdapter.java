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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swt.event;

/**
 * An adapter class that implements all of the method
 * defined in the {@code MapPaneListener} interface as empty methods, allowing sub-classes
 * to just override the methods they need. 
 *
 * @author Michael Bedward
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public class MapPaneAdapter implements MapPaneListener {

    /**
     * Called by the map pane when a new map context has been set
     *
     * @param ev the event
     */
    public void onNewContext(MapPaneEvent ev) {}

    /**
     * Called by the map pane when a new renderer has been set
     *
     * @param ev the event
     */
    public void onNewRenderer(MapPaneEvent ev) {}

    /**
     * Called by the map pane when it has been resized
     *
     * @param ev the event
     */
    public void onResized(MapPaneEvent ev) {}

    /**
     * Called by the map pane when its display area has been
     * changed e.g. by zooming or panning
     *
     * @param ev the event
     */
    public void onDisplayAreaChanged(MapPaneEvent ev) {}

    /**
     * Called by the map pane when it has started rendering features
     *
     * @param ev the event
     */
    public void onRenderingStarted(MapPaneEvent ev) {}

    /**
     * Called by the map pane when it has stopped rendering features
     *
     * @param ev the event
     */
    public void onRenderingStopped(MapPaneEvent ev) {}

    /**
     * Called by the map pane when it is rendering features. The
     * event will be carrying data: a floating point value between
     * 0 and 1 indicating rendering progress.
     *
     * @param ev the event
     */
    public void onRenderingProgress(MapPaneEvent ev) {}

}
