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

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

import org.geotools.map.Layer;
import org.geotools.swing.JMapPane;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.testutils.GraphicsTestBase;
import org.geotools.swing.testutils.MockMapContent;
import org.geotools.swing.testutils.WaitingMapPaneListener;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JPanelFixture;

import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Base class for tests of map pane cursor tools. Sets up the map pane and test data.
 * Extends {@linkplain GraphicsTestBase} to install the FEST error-detecting repaint
 * manager.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public abstract class CursorToolTestBase extends GraphicsTestBase {

    // Allow a long time for initial rendering of the test data
    protected static final long RENDERING_TIMEOUT = 5000;
    
    // Allow shorter time for event handling
    protected static final long EVENT_TIMEOUT = 1000;
    
    protected static final Rectangle SCREEN = new Rectangle(300, 300);
    
    protected static final double TOL = 1.0e-8;

    protected MockMapContent mapContent;
    protected JMapPane mapPane;
    protected JPanelFixture mapPaneFixture;
    protected WaitingMapPaneListener listener;


    @Before
    public void setupPaneAndTool() throws Exception {
        mapContent = new MockMapContent();
        mapContent.addLayer(getTestLayer());
        JFrame frame = GuiActionRunner.execute(new GuiQuery<JFrame>() {

            @Override
            protected JFrame executeInEDT() throws Throwable {
                JFrame frame = new JFrame("Cursor tool test");
                mapPane = new JMapPane(mapContent);
                mapPane.setPreferredSize(new Dimension(ZoomInToolTest.SCREEN.width, ZoomInToolTest.SCREEN.height));
                frame.add(mapPane);
                return frame;
            }
        });
        
        listener = new WaitingMapPaneListener();
        mapPane.addMapPaneListener(listener);
        windowFixture = new FrameFixture(frame);
        mapPaneFixture = new JPanelFixture(windowFixture.robot, mapPane);
        listener.setExpected(MapPaneEvent.Type.RENDERING_STOPPED);
        ((FrameFixture) windowFixture).show();
        assertTrue(listener.await(MapPaneEvent.Type.RENDERING_STOPPED, ZoomInToolTest.RENDERING_TIMEOUT));
    }
    
    protected abstract Layer getTestLayer() throws Exception;
}
