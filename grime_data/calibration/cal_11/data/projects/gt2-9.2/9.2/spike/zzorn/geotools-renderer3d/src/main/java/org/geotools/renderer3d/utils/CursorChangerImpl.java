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
package org.geotools.renderer3d.utils;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * @author Hans H�ggstr�m
 */
public final class CursorChangerImpl
        implements CursorChanger
{

    //======================================================================
    // Private Fields

    private Component myComponent = null;
    private Cursor myOriginalCursor = null;

    private Stack<CursorEntry> myCursorStack = new Stack<CursorEntry>();

    //======================================================================
    // Private Constants

    private static final Cursor EMPTY_CURSOR = createEmptyCursor();

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * Creates a new CursorChangerImpl.  You can use setComponent to set the component to manage.
     */
    public CursorChangerImpl()
    {
    }


    /**
     * @param component some component whose cursor should be managed by this cursor changer.
     */
    public CursorChangerImpl( final Component component )
    {
        setComponent( component );
    }

    //----------------------------------------------------------------------
    // CursorChanger Implementation

    public void setCursor( Object cursorSetter, Cursor cursor )
    {
        ParameterChecker.checkNotNull( cursorSetter, "cursorSetter" );

        myCursorStack.push( new CursorEntry( cursorSetter, cursor ) );
        setCursor( cursor );
    }


    public void resetCursor( final Object cursorSetter )
    {
        ParameterChecker.checkNotNull( cursorSetter, "cursorSetter" );

        // If the setter is topmost, change the cursor to the previous cursor.
        if ( !myCursorStack.isEmpty() && myCursorStack.peek().cursorSetter.equals( cursorSetter ) )
        {
            myCursorStack.pop();

            if ( myCursorStack.isEmpty() )
            {
                setCursor( myOriginalCursor );
            }
            else
            {
                setCursor( myCursorStack.peek().cursor );
            }
        }
        else
        {
            // Otherwise just remove the entry from the stack
            removeEntry( cursorSetter );
        }
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    /**
     * @param component some component whose cursor should be managed by this cursor changer.
     */
    public void setComponent( final Component component )
    {
        ParameterChecker.checkNotNull( component, "component" );

        // Restore cursor for previous component
        if ( myComponent != null )
        {
            myComponent.setCursor( myOriginalCursor );
        }

        // Set new component
        myComponent = component;
        myOriginalCursor = myComponent.getCursor();

        // Set latest cursor
        if ( !myCursorStack.isEmpty() )
        {
            setCursor( myCursorStack.peek().cursor );
        }
    }

    //======================================================================
    // Private Methods

    private void setCursor( Cursor cursor )
    {
        if ( cursor == null )
        {
            cursor = EMPTY_CURSOR;
        }

        if ( myComponent != null )
        {
            myComponent.setCursor( cursor );
        }
    }


    private static Cursor createEmptyCursor()
    {
        final BufferedImage emptyImage = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB );
        emptyImage.setRGB( 0, 0, 0 );
        return Toolkit.getDefaultToolkit().createCustomCursor( emptyImage, new Point( 0, 0 ), "EmptyCursor" );
    }


    private void removeEntry( final Object cursorSetter )
    {
        CursorEntry entryToRemove = null;
        for ( CursorEntry cursorEntry : myCursorStack )
        {
            if ( cursorEntry.cursorSetter.equals( cursorSetter ) )
            {
                entryToRemove = cursorEntry;
                break;
            }
        }

        if ( entryToRemove != null )
        {
            myCursorStack.remove( entryToRemove );
        }
    }

    //======================================================================
    // Inner Classes

    private static final class CursorEntry
    {

        //======================================================================
        // Non-Private Fields

        final Object cursorSetter;
        final Cursor cursor;

        //======================================================================
        // Public Methods

        //----------------------------------------------------------------------
        // Constructors

        public CursorEntry( final Object cursorSetter, final Cursor cursor )
        {
            this.cursorSetter = cursorSetter;
            this.cursor = cursor;
        }

        //----------------------------------------------------------------------
        // Caononical Methods

        public boolean equals( final Object o )
        {
            if ( this == o )
            {
                return true;
            }
            if ( o == null || getClass() != o.getClass() )
            {
                return false;
            }

            final CursorEntry that = (CursorEntry) o;

            if ( cursorSetter != null ? !cursorSetter.equals( that.cursorSetter ) : that.cursorSetter != null )
            {
                return false;
            }

            return true;
        }


        public int hashCode()
        {
            return ( cursorSetter != null ? cursorSetter.hashCode() : 0 );
        }

    }

}
