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
import org.geotools.renderer3d.utils.CursorChanger;

import javax.swing.event.MouseInputAdapter;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


/**
 * Contains common functionality for navigationGestureListeners.
 *
 * @author Hans H�ggstr�m
 */
public abstract class AbstractNavigationGesture
        extends MouseInputAdapter
        implements NavigationGesture
{

    //======================================================================
    // Private Fields

    private CameraAccessor myCameraAccessor = null;
    private CursorChanger myCursorChanger;
    private Robot myRobot;
    private Component myComponent;

    //======================================================================
    // Non-Private Fields

    private final float mySensitivity;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // MouseWheelListener Implementation

    public void mouseWheelMoved( final MouseWheelEvent e )
    {
        // Override if needed
    }

    //----------------------------------------------------------------------
    // NavigationGesture Implementation

    public void init( final Component component,
                      final CursorChanger cursorChanger,
                      final CameraAccessor cameraAccessor )
    {
        setComponent( component );
        setCursorChanger( cursorChanger );
        setCameraAccessor( cameraAccessor );
    }


    public void deInit()
    {
        setComponent( null );
        setCursorChanger( null );
        setCameraAccessor( null );
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    /**
     * @return a scaling factor to apply to the gesture.
     */
    public final float getSensitivity()
    {
        return mySensitivity;
    }

    //======================================================================
    // Protected Methods

    //----------------------------------------------------------------------
    // Protected Constructors

    protected AbstractNavigationGesture( final float sensitivity )
    {
        try
        {
            myRobot = new Robot();
        }
        catch ( AWTException e )
        {
            e.printStackTrace();
        }

        mySensitivity = sensitivity;
    }


    /**
     * Moves the mouse position to the specified canvas coordinates
     */
    protected final void setMousePos( int x, int y )
    {
        final Point locationOnScreen = myComponent.getLocationOnScreen();
        myRobot.mouseMove( locationOnScreen.x + x, locationOnScreen.y + y );
    }


    /**
     * @return an interface that can be used to change the mouse cursor on the 3D canvas we are rendering to (or hide it).
     */
    protected final CursorChanger getCursorChanger()
    {
        return myCursorChanger;
    }


    private void setCursorChanger( final CursorChanger cursorChanger )
    {
        if ( cursorChanger == null && myCursorChanger != null )
        {
            myCursorChanger.resetCursor( this );
        }

        myCursorChanger = cursorChanger;
    }


    /**
     * @return the camera that has been assigned to this navigation gesture listener, or null if no camera has yet been assigned.
     */
    protected final Camera getCamera()
    {
        if ( myCameraAccessor != null )
        {
            return myCameraAccessor.getCamera();
        }
        else
        {
            return null;
        }
    }


    /**
     * @return a multiplication factor calculated from the altitude, used to adjust the move and pan operations.
     */
    protected final float getAltitudeFactor()
    {
        final Camera camera = getCamera();
        if ( camera != null )
        {
            final float altitude = getAltitudeAt( camera.getLocation() );

            // Less movement lower, more higher.  Use a non linear function (square root) of the altitude.
            return (float) ( 0.4 * Math.sqrt( Math.abs( altitude ) ) + 0.01f );
        }
        else
        {
            return 1;
        }
    }


    protected boolean isMouseButtonPressed( final MouseEvent e, final int buttonDownMask )
    {
        return ( e.getModifiersEx() & buttonDownMask ) != 0;
    }

    //======================================================================
    // Private Methods

    /**
     * REFACTOR: Move to some terrain or map class and make public.
     *
     * @param location A location to calculate the altitude of.
     *
     * @return the altitude of the specified location above the ground.  The accuracy can be calculated relative to the altitude,
     *         if the location is closer to the ground, higher accuracy is used than if it is further from the ground.
     */
    private float getAltitudeAt( final Vector3f location )
    {
        return location.z - getTerrainHeightAt( location );
    }


    /**
     * REFACTOR: Move to some terrain or map class and make public.
     *
     * @param location some location in the world.  Usually only the x and y values are used, z is ignored.
     *                 The z value can be used to determine the accuracy to calculate the terrain height at.  If the location is far above (or below) the ground,
     *                 less accuracy is needed, while if it is close to the ground, greater accuracy is needed.
     *
     * @return the terrain height at the specified location.
     */
    private float getTerrainHeightAt( final Vector3f location )
    {
        // TODO: Get terrain height at the specified location
        return (float) 0;
    }


    private void setComponent( final Component component )
    {
        if ( myComponent != null )
        {
            myComponent.removeMouseMotionListener( this );
            myComponent.removeMouseListener( this );
            myComponent.removeMouseWheelListener( this );
        }

        myComponent = component;

        component.addMouseMotionListener( this );
        component.addMouseListener( this );
        component.addMouseWheelListener( this );
    }


    private void setCameraAccessor( final CameraAccessor cameraAccessor )
    {
        myCameraAccessor = cameraAccessor;
    }

}
