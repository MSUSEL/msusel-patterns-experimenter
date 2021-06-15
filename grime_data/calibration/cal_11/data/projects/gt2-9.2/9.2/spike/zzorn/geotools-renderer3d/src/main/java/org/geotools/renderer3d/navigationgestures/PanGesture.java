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

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * Implements a panning gesture for the 3D renderer.
 * <p/>
 * Pan / move across map with left mouse button drag (will actually move camera along the left/right,
 * up/down axes of the camera). (A single left mouse button click will select / click on an item on map).
 * The drag has some inertia, so a quick drag will move the camera faster than a slow one.
 * The camera is kept above the ground level at all times though.
 * <p/>
 * Also scale pan amount by altitude.
 *
 * @author Hans H�ggstr�m
 */
public final class PanGesture
        extends AbstractDragGesture
{

    //======================================================================
    // Private Constants

    private static final float DEFAULT_PAN_SENSITIVITY = 0.1f;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    public PanGesture()
    {
        super( DEFAULT_PAN_SENSITIVITY, MouseEvent.BUTTON3, InputEvent.BUTTON3_DOWN_MASK );
    }

    //======================================================================
    // Protected Methods

    protected void applyDragGesture( final Camera camera, final float deltaX, final float deltaY )
    {
        final float altitudeFactor = getAltitudeFactor();

        // TODO: Add inertia and acceleration
        // TODO: Add ground collision detection and keep the camera above ground.

        final Vector3f newLocation = new Vector3f( camera.getLocation() );

        newLocation.scaleAdd( -deltaX * altitudeFactor, camera.getLeft(), newLocation );
        newLocation.scaleAdd( -deltaY * altitudeFactor, camera.getUp(), newLocation );

        camera.setLocation( newLocation );
    }

}
