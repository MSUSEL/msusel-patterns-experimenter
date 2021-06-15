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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer3d.navigationgestures;


import org.geotools.renderer3d.utils.CursorChanger;

import javax.swing.event.MouseInputListener;
import java.awt.Component;
import java.awt.event.MouseWheelListener;

/**
 * An interface for handling navigation gestures done to the 3D view.
 *
 * @author Hans H�ggstr�m
 */
public interface NavigationGesture
        extends MouseInputListener, MouseWheelListener
{
    /**
     * Initializes the navigation gesture.
     *
     * @param component      the component that the gesture should listen to.  Can be asked e.g. the size and position.
     *                       Might also be used to add rubber band graphics or similar on top of the component
     * @param cursorChanger  an interface that can be used to change the mouse cursor on the 3D canvas rendered to.
     * @param cameraAccessor Can be asked for the camera that the navigation gesture listener should modify when gestures happen.
     */
    void init( Component component,
               CursorChanger cursorChanger,
               CameraAccessor cameraAccessor );

    /**
     * De-initializes the navigation gesture.  The navigation gesture should stop listening to the component, and should
     * remove its cursor settings.
     */
    void deInit();
}
