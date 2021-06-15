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
package org.geotools.swing;

import java.awt.Graphics2D;
import org.geotools.map.Layer;
import org.geotools.renderer.GTRenderer;

/**
 *
 * @author michael
 *
 * @source $URL$
 */
public class RenderingOperands {

    private final Layer layer;
    private final Graphics2D graphics;
    private final GTRenderer renderer;

    public RenderingOperands(Layer layer, Graphics2D graphics) {
        this(layer, graphics, null);
    }

    public RenderingOperands(Layer layer, Graphics2D graphics, GTRenderer renderer) {
        this.layer = layer;
        this.graphics = graphics;
        this.renderer = renderer;
    }

    public Layer getLayer() {
        return layer;
    }

    public Graphics2D getGraphics() {
        return graphics;
    }

    public GTRenderer getRenderer() {
        return renderer;
    }
}
