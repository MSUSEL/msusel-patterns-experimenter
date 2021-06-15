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

package org.geotools.swing.tool;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.TestDataUtils;

import org.fest.swing.core.MouseButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Tests for the zoom-in cursor tool.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class ZoomInToolTest extends CursorToolTestBase {
    private ZoomInTool tool;

    @Before
    public void setup() {
        tool = new ZoomInTool();
    }
    
    @Test
    public void clickZoomAtCentreOfMap() throws Exception {
        ReferencedEnvelope startEnv = mapPane.getDisplayArea();
        mapPane.setCursorTool(tool);
        
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        mapPaneFixture.click();
        
        assertTrue( listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, EVENT_TIMEOUT) );
        ReferencedEnvelope endEnv = mapPane.getDisplayArea();
        
        double xZoom = startEnv.getWidth() / endEnv.getWidth();
        double yZoom = startEnv.getHeight() / endEnv.getHeight();
        
        assertEquals(AbstractZoomTool.DEFAULT_ZOOM_FACTOR, xZoom, TOL);
        assertEquals(AbstractZoomTool.DEFAULT_ZOOM_FACTOR, yZoom, TOL);
    }

    @Test
    public void zoomInToolSupportsDragging() throws Exception {
        assertTrue(tool.drawDragBox());
    }
    
    /*
     * Note: this test relies on the map pane being displayed with equal
     * width and height
     */
    @Test
    public void drawBoxToZoom() throws Exception {
        mapPane.setCursorTool(tool);
        
        Rectangle dragBoxRect = new Rectangle(
                SCREEN.width / 4, SCREEN.height / 4,
                SCREEN.width / 2, SCREEN.height / 2);

        AffineTransform startTransform = mapPane.getScreenToWorldTransform();
        Rectangle2D expectedRect = startTransform.createTransformedShape(dragBoxRect).getBounds2D();

        ReferencedEnvelope expectedEnv = new ReferencedEnvelope(expectedRect, 
                mapPane.getDisplayArea().getCoordinateReferenceSystem());
        
        Point screenPos = mapPaneFixture.component().getLocationOnScreen();
        
        Point mouseStartPos = new Point(
                screenPos.x + dragBoxRect.x, 
                screenPos.y + dragBoxRect.y);
        
        Point mouseEndPos = new Point(
                screenPos.x + (int) dragBoxRect.getMaxX(), 
                screenPos.y + (int) dragBoxRect.getMaxY());
        
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        
        mapPaneFixture.robot.pressMouse(mouseStartPos, MouseButton.LEFT_BUTTON);
        mapPaneFixture.robot.moveMouse(mouseEndPos);
        mapPaneFixture.robot.releaseMouseButtons();
        
        assertTrue( listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, EVENT_TIMEOUT) );
        assertTrue(expectedEnv.boundsEquals2D(mapPane.getDisplayArea(), TOL));
    }

    @Override
    protected Layer getTestLayer() throws Exception {
        return TestDataUtils.getPointLayer();
    }

}
