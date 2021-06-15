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
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.geotools.swt.SwtMapPane;
import org.geotools.swt.event.MapMouseEvent;
import org.geotools.swt.utils.CursorManager;
import org.geotools.swt.utils.Messages;

/**
 * A map panning tool for {@link SwtMapPane}.
 * 
 * <p>Allows the user to drag the map
 * with the mouse.
 * 
 * @author Michael Bedward
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public class PanTool extends CursorTool {

    /** Tool name */
    public static final String TOOL_NAME = Messages.getString("tool_name_pan");
    /** Tool tip text */
    public static final String TOOL_TIP = Messages.getString("tool_tip_pan");

    private Cursor cursor;

    private Point panePos;
    boolean panning;

    /**
     * Constructs a new pan tool. To activate the tool only on certain
     * mouse events provide a single mask, e.g. {@link SWT#BUTTON1}, or
     * a combination of multiple SWT-masks.
     *
     * @param triggerButtonMask Mouse button which triggers the tool's activation
     * or {@value #ANY_BUTTON} if the tool is to be triggered by any button
     */
    public PanTool(int triggerButtonMask) {
        super(triggerButtonMask);

        cursor = CursorManager.getInstance().getPanCursor();

        panning = false;
    }

    /**
     * Constructs a new pan tool which is triggered by any mouse button.
     */
    public PanTool() {
        this(CursorTool.ANY_BUTTON);
    }

    /**
     * Respond to a mouse button press event from the map mapPane. This may
     * signal the start of a mouse drag. Records the event's window position.
     * @param ev the mouse event
     */
    @Override
    public void onMousePressed( MapMouseEvent ev ) {

        if ( ! isTriggerMouseButton(ev)) {
            return;
        }

        panePos = ev.getPoint();
        panning = true;
    }

    /**
     * Respond to a mouse dragged event. Calls {@link org.geotools.swing.JMapPane#moveImage()}
     * @param ev the mouse event
     */
    @Override
    public void onMouseDragged( MapMouseEvent ev ) {
        if (panning) {
            Point pos = ev.getPoint();
            if (!pos.equals(panePos)) {
                getMapPane().moveImage(pos.x - panePos.x, pos.y - panePos.y);
                panePos = pos;
            }
        }
    }

    /**
     * If this button release is the end of a mouse dragged event, requests the
     * map mapPane to repaint the display
     * @param ev the mouse event
     */
    @Override
    public void onMouseReleased( MapMouseEvent ev ) {
        if (panning) {
            panning = false;
            getMapPane().redraw();
        }
    }

    /**
     * Get the mouse cursor for this tool
     */
    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public boolean canDraw() {
        return false;
    }

    public boolean canMove() {
        return true;
    }
}
