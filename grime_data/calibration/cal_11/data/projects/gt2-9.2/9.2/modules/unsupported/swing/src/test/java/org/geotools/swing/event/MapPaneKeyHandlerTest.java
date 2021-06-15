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

package org.geotools.swing.event;

import org.geotools.swing.testutils.GraphicsTestBase;
import java.awt.Frame;
import java.awt.Dimension;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.fest.swing.core.KeyPressInfo;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import static org.fest.swing.core.KeyPressInfo.*;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.swing.testutils.GraphicsTestRunner;
import org.geotools.swing.testutils.MockMapPane;
import org.opengis.geometry.Envelope;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * Unit tests for MapPaneKeyHandler. Requires graphics environment.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
@RunWith(GraphicsTestRunner.class)
public class MapPaneKeyHandlerTest extends GraphicsTestBase<Frame> {
    private static final long WAIT_TIMEOUT = 1000;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 150;

    private MapPaneKeyHandler handler;
    private MockMapPane2 mapPane;
    
    @Before
    public void setup() {
        TestFrame frame = GuiActionRunner.execute(new GuiQuery<TestFrame>(){
            @Override
            protected TestFrame executeInEDT() throws Throwable {
                mapPane = new MockMapPane2();
                mapPane.setName("pane");
                handler = new MapPaneKeyHandler(mapPane);
                mapPane.addKeyListener(handler);
                
                TestFrame frame = new TestFrame(mapPane);
                return frame;
            }
        });
        
        windowFixture = new FrameFixture(frame);
        ((FrameFixture) windowFixture).show(new Dimension(WIDTH, HEIGHT));
    }
    
    
    @Test
    public void scrollLeft() throws Exception {
        assertScroll(MapPaneKeyHandler.Action.SCROLL_LEFT, 1, 0);
    }

    @Test
    public void scrollRight() throws Exception {
        assertScroll(MapPaneKeyHandler.Action.SCROLL_RIGHT, -1, 0);
    }
    
    @Test
    public void scrollUp() throws Exception {
        assertScroll(MapPaneKeyHandler.Action.SCROLL_UP, 0, 1);
    }

    @Test
    public void scrollDown() throws Exception {
        assertScroll(MapPaneKeyHandler.Action.SCROLL_DOWN, 0, -1);
    }
    
    @Ignore("problem with this test")
    @Test
    public void zoomIn() throws Exception {
        ReferencedEnvelope startEnv = mapPane.getDisplayArea();
        
        KeyPressInfo info = getKeyPressInfo(MapPaneKeyHandler.Action.ZOOM_IN);
        windowFixture.panel("pane").pressAndReleaseKey(info);
        
        assertTrue(mapPane.latch.await(WAIT_TIMEOUT, TimeUnit.MILLISECONDS));
        
        ReferencedEnvelope endEnv = mapPane.getDisplayArea();
        assertEquals(-1, sign(endEnv.getWidth() - startEnv.getWidth()));
    }
    
    private void assertScroll(MapPaneKeyHandler.Action action, int expectedDx, int expectedDy) 
            throws Exception {
        
        KeyPressInfo info = getKeyPressInfo(action);
        windowFixture.panel("pane").pressAndReleaseKey(info);
        
        assertTrue(mapPane.latch.await(WAIT_TIMEOUT, TimeUnit.MILLISECONDS));
        assertEquals(sign(expectedDx), sign(mapPane.dx));
        assertEquals(sign(expectedDy), sign(mapPane.dy));
    }
    
    private int sign(int i) {
        return (i < 0 ? -1 : (i > 0 ? 1 : 0));
    }
    
    private int sign(double d) {
        return Double.compare(d, 0);
    }

    /**
     * Looks up the key binding for an action and converts it to a FEST
     * KeyPressInfo object.
     * 
     * @param action the action
     * @return a new KeyPressInfo object
     */
    private KeyPressInfo getKeyPressInfo(MapPaneKeyHandler.Action action) {
        KeyInfo keyId = handler.getBindingForAction(action);
        return keyCode(keyId.getKeyCode()).modifiers(keyId.getModifiers());
    }
    
    /**
     * A frame containing a mock map pane.
     */
    private static class TestFrame extends JFrame {
        public TestFrame(final MockMapPane mapPane) {
            add(mapPane);
        }
    }
    
    private static class MockMapPane2 extends MockMapPane {
        CountDownLatch latch = new CountDownLatch(1);
        
        ReferencedEnvelope env = new ReferencedEnvelope(0, 100, 0, 100, null);
        int dx = 0;
        int dy = 0;
        private boolean gotReset = false;
        
        @Override
        public void moveImage(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
            latch.countDown();
        }

        @Override
        public ReferencedEnvelope getDisplayArea() {
            return new ReferencedEnvelope(env);
        }

        @Override
        public void setDisplayArea(Envelope envelope) {
            this.env = new ReferencedEnvelope(envelope);
            latch.countDown();
        }
        
        @Override
        public void reset() {
            gotReset = true;
            latch.countDown();
        }
    }
}
