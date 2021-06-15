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

import com.jme.renderer.Camera;

import java.awt.event.MouseEvent;

/**
 * Common functionality for gestures that involve listening to drags.
 *
 * @author Hans H�ggstr�m
 */
public abstract class AbstractDragGesture
        extends AbstractNavigationGesture
{

    //======================================================================
    // Private Fields

    private final int myButtonDownMask;
    private final int myButton;

    private int myOldX = 0;
    private int myOldY = 0;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // MouseListener Implementation

    public void mousePressed( final MouseEvent e )
    {
        if ( isMouseButtonPressed( e, myButtonDownMask ) )
        {
            myOldX = e.getX();
            myOldY = e.getY();

            getCursorChanger().setCursor( this, null );
        }
    }


    public void mouseReleased( final MouseEvent e )
    {
        if ( e.getButton() == myButton )
        {
            getCursorChanger().resetCursor( this );
        }
    }

    //----------------------------------------------------------------------
    // MouseMotionListener Implementation

    public void mouseDragged( final MouseEvent e )
    {
        if ( isMouseButtonPressed( e, myButtonDownMask ) )
        {
            final Camera camera = getCamera();
            if ( camera != null )
            {
                // Calculate mouse movement
                final int currentX = e.getX();
                final int currentY = e.getY();
                final int deltaXInt = currentX - myOldX;
                final int deltaYInt = currentY - myOldY;

                if ( deltaXInt != 0 || deltaYInt != 0 )
                {
                    final float deltaX = deltaXInt * getSensitivity();
                    final float deltaY = deltaYInt * getSensitivity();

                    // Do the drag bussiness logic
                    applyDragGesture( camera, deltaX, deltaY );

                    // Reset mouse position
                    setMousePos( myOldX, myOldY );
                }
            }
        }
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    //======================================================================
    // Protected Methods

    //----------------------------------------------------------------------
    // Protected Constructors

    /**
     * @param sensitivity    a sensitivity value for this gesture.
     * @param button         button whose drags we should listen to.
     * @param buttonDownMask the down mask for the button whose drags we should listen to.
     */
    protected AbstractDragGesture( final float sensitivity, final int button, final int buttonDownMask )
    {
        super( sensitivity );
        myButton = button;
        myButtonDownMask = buttonDownMask;
    }

    //----------------------------------------------------------------------
    // Abstract Protected Methods

    /**
     * Called from the drag handling code.
     *
     * @param camera a reference to the camera
     * @param deltaX the sensitivity scaled movement along the x axis of the mouse since the last call or the start of the gesture.
     * @param deltaY the sensitivity scaled movement along the y axis of the mouse since the last call or the start of the gesture.
     */
    protected abstract void applyDragGesture( Camera camera, float deltaX, float deltaY );

}
