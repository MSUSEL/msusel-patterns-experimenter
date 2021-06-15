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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing.testutils;

import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.event.MapMouseListener;

/**
 * A MapMouseListener that can be set to expect specified events
 * and test if they are received.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class WaitingMapMouseListener 
        extends WaitingListener<MapMouseEvent, WaitingMapMouseListener.Type> 
        implements MapMouseListener {
    
    public static enum Type {
        CLICKED,
        DRAGGED,
        ENTERED,
        EXITED,
        MOVED,
        PRESSED,
        RELEASED,
        WHEEL_MOVED;
    }

    public WaitingMapMouseListener() {
        super(Type.values().length);
    }
    

    @Override
    public void onMouseClicked(MapMouseEvent ev) {
        catchEvent(Type.CLICKED.ordinal(), ev);
    }

    @Override
    public void onMouseDragged(MapMouseEvent ev) {
        catchEvent(Type.DRAGGED.ordinal(), ev);
    }

    @Override
    public void onMouseEntered(MapMouseEvent ev) {
        catchEvent(Type.ENTERED.ordinal(), ev);
    }

    @Override
    public void onMouseExited(MapMouseEvent ev) {
        catchEvent(Type.EXITED.ordinal(), ev);
    }

    @Override
    public void onMouseMoved(MapMouseEvent ev) {
        catchEvent(Type.MOVED.ordinal(), ev);
    }

    @Override
    public void onMousePressed(MapMouseEvent ev) {
        catchEvent(Type.PRESSED.ordinal(), ev);
    }

    @Override
    public void onMouseReleased(MapMouseEvent ev) {
        catchEvent(Type.RELEASED.ordinal(), ev);
    }

    @Override
    public void onMouseWheelMoved(MapMouseEvent ev) {
        catchEvent(Type.WHEEL_MOVED.ordinal(), ev);
    }
    
}
