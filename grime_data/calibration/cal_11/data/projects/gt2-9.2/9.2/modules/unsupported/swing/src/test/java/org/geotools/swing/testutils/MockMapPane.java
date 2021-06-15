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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.testutils;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.DefaultMapMouseEventDispatcher;
import org.geotools.swing.event.MapMouseEventDispatcher;
import org.geotools.swing.event.MapMouseListener;
import org.geotools.swing.event.MapPaneListener;
import org.geotools.swing.tool.CursorTool;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Mock map pane class for testing in a headless environment. 
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class MockMapPane extends JPanel implements MapPane {
    private MapContent mapContent;
    private List<MapPaneListener> mapPaneListeners;
    private MapMouseEventDispatcher mouseEventDispatcher;

    public MockMapPane() {
        mapPaneListeners = new ArrayList<MapPaneListener>();
        mouseEventDispatcher = new DefaultMapMouseEventDispatcher(this);
    }
    
    @Override
    public void setMapContent(MapContent content) {
        mapContent = content;
        mapContent.getViewport().setMatchingAspectRatio(true);
    }

    @Override
    public MapContent getMapContent() {
        return mapContent;
    }

    @Override
    public ReferencedEnvelope getDisplayArea() {
        return mapContent.getViewport().getBounds();
    }

    @Override
    public void setDisplayArea(Envelope envelope) {
        CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
        if (crs == null) {
            // assume that it is the current CRS
            crs = mapContent.getCoordinateReferenceSystem();
        }
        ReferencedEnvelope refEnv = new ReferencedEnvelope(
                envelope.getMinimum(0), envelope.getMaximum(0), 
                envelope.getMinimum(1), envelope.getMaximum(1), crs);
        mapContent.getViewport().setBounds(refEnv);
    }
    
    public void setScreenArea(Rectangle screenArea) {
        mapContent.getViewport().setScreenArea(screenArea);
    }

    @Override
    public void reset() {
        // do nothing
    }

    @Override
    public AffineTransform getScreenToWorldTransform() {
        return mapContent.getViewport().getScreenToWorld();
    }

    @Override
    public AffineTransform getWorldToScreenTransform() {
        return mapContent.getViewport().getWorldToScreen();
    }
    
    @Override
    public void addMapPaneListener(MapPaneListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("null listener arg");
        }
        mapPaneListeners.add(listener);
    }

    @Override
    public void removeMapPaneListener(MapPaneListener listener) {
        if (listener != null) {
            mapPaneListeners.remove(listener);
        }
    }

    @Override
    public void addMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("null listener arg");
        }
        mouseEventDispatcher.addMouseListener(listener);
    }

    @Override
    public void removeMouseListener(MapMouseListener listener) {
        if (listener != null) {
            mouseEventDispatcher.removeMouseListener(listener);
        }
    }

    @Override
    public CursorTool getCursorTool() {
        return null;
    }

    @Override
    public void setCursorTool(CursorTool tool) {
        // empty method
    }

    @Override
    public void moveImage(int dx, int dy) {
        // empty method
    }

    @Override
    public MapMouseEventDispatcher getMouseEventDispatcher() {
        return mouseEventDispatcher;
    }

    @Override
    public void setMouseEventDispatcher(MapMouseEventDispatcher dispatcher) {
        mouseEventDispatcher = dispatcher;
    }

}
