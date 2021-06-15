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
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

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
 * Tests for the pan cursor tool.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class PanToolTest extends CursorToolTestBase {
    private PanTool tool;
    
    @Before
    public void setup() {
        tool = new PanTool();
    }
    
    @Test
    public void doesNotDrawDragBox() throws Exception {
        assertFalse(tool.drawDragBox());
    }
    
    @Test
    public void dragPanMap() throws Exception {
        ReferencedEnvelope startEnv = mapPane.getDisplayArea();
        AffineTransform tr = mapPane.getScreenToWorldTransform();

        Point startWindowPos = new Point(SCREEN.width / 4, SCREEN.height / 4);
        Point endWindowPos = new Point(SCREEN.width / 2, SCREEN.height / 2);
        
        Point screenPos = mapPaneFixture.component().getLocationOnScreen();
        
        Point mouseStartPos = new Point(
                screenPos.x + startWindowPos.x, 
                screenPos.y + startWindowPos.y);
        
        Point mouseEndPos = new Point(
                screenPos.x + endWindowPos.x,
                screenPos.y + endWindowPos.y);
        
        listener.setExpected(MapPaneEvent.Type.DISPLAY_AREA_CHANGED);
        
        mapPane.setCursorTool(tool);
        mapPaneFixture.robot.pressMouse(mouseStartPos, MouseButton.LEFT_BUTTON);
        mapPaneFixture.robot.moveMouse(mouseEndPos);
        mapPaneFixture.robot.releaseMouseButtons();
        
        assertTrue( listener.await(MapPaneEvent.Type.DISPLAY_AREA_CHANGED, EVENT_TIMEOUT) );
        
        ReferencedEnvelope endEnv = mapPane.getDisplayArea();

        Point2D expectedDelta = tr.deltaTransform(new Point(
                startWindowPos.x - endWindowPos.x, 
                startWindowPos.y - endWindowPos.y), 
                null);
        
        assertEquals(startEnv.getMinX() + expectedDelta.getX(), endEnv.getMinX(), TOL);
        assertEquals(startEnv.getMinY() + expectedDelta.getY(), endEnv.getMinY(), TOL);
    }

    @Override
    protected Layer getTestLayer() throws Exception {
        return TestDataUtils.getPointLayer();
    }

}
