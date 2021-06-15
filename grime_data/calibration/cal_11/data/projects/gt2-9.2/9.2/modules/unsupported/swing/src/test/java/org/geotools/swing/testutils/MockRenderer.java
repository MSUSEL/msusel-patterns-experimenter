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

import com.vividsolutions.jts.geom.Envelope;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;

/**
 * A simple mock GTRenderer.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class MockRenderer implements GTRenderer {
    private MapContent mapContent;
    private long paintTime;
    private boolean verbose;
    
    private CountDownLatch paintLatch = new CountDownLatch(0);
    private Lock lock = new ReentrantLock();
    
    public MockRenderer() {
        this(null);
    }
    
    public MockRenderer(MapContent mapContent) {
        this.mapContent = mapContent;
        this.paintTime = 0;
    }
    
    /**
     * Sets the time that the mock renderer will pretend to paint.
     * 
     * @param millis time in milliseconds
     */
    public void setPaintTime(long millis) {
        paintTime = millis < 0 ? 0 : millis;
    }
    
    public void setVerbose(boolean b) {
        verbose = b;
    }

    @Override
    public void stopRendering() {
        lock.lock();
        try {
            paintLatch.countDown();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addRenderListener(RenderListener listener) {
    }

    @Override
    public void removeRenderListener(RenderListener listener) {
    }

    @Override
    public void setJava2DHints(RenderingHints hints) {
    }

    @Override
    public RenderingHints getJava2DHints() {
        return new Hints();
    }

    @Override
    public void setRendererHints(Map<Object, Object> hints) {
    }

    @Override
    public Map<Object, Object> getRendererHints() {
        return Collections.emptyMap();
    }

    @Override
    public void setContext(MapContext context) {
        throw new UnsupportedOperationException("Should not be called");
    }

    @Override
    public void setMapContent(MapContent mapContent) {
        this.mapContent = mapContent;
    }

    @Override
    public MapContext getContext() {
        throw new UnsupportedOperationException("Should not be called");
    }

    @Override
    public MapContent getMapContent() {
        return mapContent;
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, AffineTransform worldToScreen) {
        pretendToPaint();
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea) {
        pretendToPaint();
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope mapArea) {
        pretendToPaint();
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, Envelope mapArea, AffineTransform worldToScreen) {
        pretendToPaint();
    }

    @Override
    public void paint(Graphics2D graphics, Rectangle paintArea, ReferencedEnvelope mapArea, AffineTransform worldToScreen) {
        pretendToPaint();
    }

    private void pretendToPaint() {
        lock.lock();
        try {
            if (verbose) {
                System.out.println("mock paint started");
                System.out.flush();
            }

            if (paintTime > 0) {
                paintLatch = new CountDownLatch(1);
            }
        } finally {
            lock.unlock();
        }
        
        boolean wasCancelled = false;
        try {
            wasCancelled = paintLatch.await(paintTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        if (verbose) {
            if (wasCancelled) {
                System.out.println("mock paint cancelled");
            } else {
                System.out.println("mock paint finished");
            }
            System.out.flush();
        }
    }
    
}
