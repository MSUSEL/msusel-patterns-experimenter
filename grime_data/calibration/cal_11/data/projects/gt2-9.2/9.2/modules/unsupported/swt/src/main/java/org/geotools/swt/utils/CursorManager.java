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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swt.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

/**
 * A singleton cursor manager.
 * 
 * <p>This takes care of creating, reusing and managing the various cursors.
 * 
 * @author Andrea Antonello (www.hydrologis.com).
 *
 *
 *
 * @source $URL$
 */
public class CursorManager {
    /** Info Cursor hotspot coordinates */
    public static final Point CURSOR_HOTSPOT_INFO = new Point(4, 4);
    /** Pan Cursor hotspot coordinates */
    public static final Point CURSOR_HOTSPOT_PAN = new Point(8, 8);
    /** Zoomin Cursor hotspot coordinates */
    public static final Point CURSOR_HOTSPOT_ZOOMIN = new Point(6, 6);
    /** Zoomout Cursor hotspot coordinates */
    public static final Point CURSOR_HOTSPOT_ZOOMOUT = new Point(6, 6);

    private Cursor currentCursor = null;

    private static CursorManager instance = null;
    private CursorManager() {
    }

    public static CursorManager getInstance() {
        if (instance == null) {
            instance = new CursorManager();
        }
        return instance;
    }

    public Cursor getArrowCursor() {
        if (currentCursor != null && !currentCursor.isDisposed())
            currentCursor.dispose();
        currentCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_ARROW);
        return currentCursor;
    }

    public Cursor getInfoCursor() {
        Image image = ImageCache.getInstance().getImage(ImageCache.IMAGE_INFO_ICON);
        setCursor(image, CURSOR_HOTSPOT_INFO);
        return currentCursor;
    }

    public Cursor getPanCursor() {
        Image image = ImageCache.getInstance().getImage(ImageCache.IMAGE_PAN);
        setCursor(image, CURSOR_HOTSPOT_PAN);
        return currentCursor;
    }

    public Cursor getZoominCursor() {
        Image image = ImageCache.getInstance().getImage(ImageCache.IMAGE_ZOOMIN);
        setCursor(image, CURSOR_HOTSPOT_ZOOMIN);
        return currentCursor;
    }

    public Cursor getZoomoutCursor() {
        Image image = ImageCache.getInstance().getImage(ImageCache.IMAGE_ZOOMOUT);
        setCursor(image, CURSOR_HOTSPOT_ZOOMOUT);
        return currentCursor;
    }

    public void setCursor( Image image, Point hotspot ) {
        if (currentCursor != null && !currentCursor.isDisposed())
            currentCursor.dispose();
        Display display = Display.getCurrent();
        currentCursor = new Cursor(display, image.getImageData(), hotspot.x, hotspot.y);
    }
}
