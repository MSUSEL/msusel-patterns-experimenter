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

package org.geotools.swt.tool;

import org.eclipse.swt.SWT;


/**
 * Abstract base class for the zoom-in and zoom-out tools. Provides getter / setter
 * methods for the zoom increment.
 * 
 * @author Michael Bedward
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */

public abstract class AbstractZoomTool extends CursorTool {
    /** The default zoom increment */
    public static final double DEFAULT_ZOOM_FACTOR = 1.5;

    /** The working zoom increment */
    protected double zoom;

    /**
     * Constructs a new abstract zoom tool. To activate the tool only on certain
     * mouse events provide a single mask, e.g. {@link SWT#BUTTON1}, or
     * a combination of multiple SWT-masks.
     *
     * @param triggerButtonMask Mouse button which triggers the tool's activation
     * or {@value #ANY_BUTTON} if the tool is to be triggered by any button
     */
     public AbstractZoomTool(int triggerButtonMask) {
         super(triggerButtonMask);
         setZoom(DEFAULT_ZOOM_FACTOR);
     }

     /**
      * Constructs a new abstract zoom tool which is triggered by any mouse button.
      */
     public AbstractZoomTool() {
         this(CursorTool.ANY_BUTTON);
     }

    
    /**
     * Get the current areal zoom increment. 
     * 
     * @return the current zoom increment as a double
     */
    public double getZoom() {
        return zoom;
    }
    
    /**
     * Set the zoom increment
     * 
     * @param newZoom the new zoom increment; values &lt;= 1.0
     * will be ignored
     * 
     * @return the previous zoom increment
     */
    public double setZoom(double newZoom) {
        double old = zoom;
        if (newZoom > 1.0d) {
            zoom = newZoom;
        }
        return old;
    }

}
