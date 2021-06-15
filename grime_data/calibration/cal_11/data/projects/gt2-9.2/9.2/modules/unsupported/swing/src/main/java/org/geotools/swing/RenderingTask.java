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

package org.geotools.swing;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.RenderListener;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A rendering task to be run by a {@code RenderingExecutor}.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class RenderingTask implements Callable<Boolean>, RenderListener {
    
    private final Graphics2D destinationGraphics;
    private final Rectangle deviceArea;
    private final ReferencedEnvelope worldArea;
    private final AffineTransform worldToScreenTransform;
    private final GTRenderer renderer;

    private final AtomicBoolean running;
    private final AtomicBoolean failed;
    private final AtomicBoolean cancelled;
    
    
    /**
     * Creates a new rendering task.
     * 
     * @param mapContent
     * @param renderer 
     * @param graphics 
     */
    public RenderingTask(MapContent mapContent, 
            Graphics2D destinationGraphics,
            GTRenderer renderer) {
        
        if (mapContent == null) {
            throw new IllegalArgumentException("mapContent must not be null");
        }
        if (renderer == null) {
            throw new IllegalArgumentException("renderer must not be null");
        }
        if (destinationGraphics == null) {
            throw new IllegalArgumentException("graphics must not be null");
        }
        
        this.destinationGraphics = destinationGraphics;
        this.deviceArea = mapContent.getViewport().getScreenArea();
        this.worldArea = mapContent.getViewport().getBounds();
        this.worldToScreenTransform = mapContent.getViewport().getWorldToScreen();
        this.renderer = renderer;
        
        running = new AtomicBoolean(false);
        failed = new AtomicBoolean(false);
        cancelled = new AtomicBoolean(false);
    }
    
    public void cancel() {
        if (running.get()) {
            renderer.stopRendering();
        }
        
        cancelled.set(true);
    }

    /**
     * Called by the executor to run this rendering task.
     *
     * @return result of the task: completed or failed
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {
        if (!cancelled.get()) {
            try {
                renderer.addRenderListener(this);
                running.set(true);
                renderer.paint(destinationGraphics,
                        deviceArea,
                        worldArea,
                        worldToScreenTransform);
            } finally {
                renderer.removeRenderListener(this);
                running.set(false);
            }
        }

        return !(isFailed() || isCancelled());
    }

    /**
     * Called by the renderer when each feature is drawn. This
     * implementation does nothing.
     *
     * @param feature the feature just drawn
     */
    @Override
    public void featureRenderer(SimpleFeature feature) {}

    /**
     * Called by the renderer on error
     *
     * @param e cause of the error
     */
    @Override
    public void errorOccurred(Exception e) {
        running.set(false);
        failed.set(true);
    }
    
    public boolean isRunning() {
        return running.get();
    }
    
    public boolean isFailed() {
        return failed.get();
    }
    
    public boolean isCancelled() {
        return cancelled.get();
    }
    
}
