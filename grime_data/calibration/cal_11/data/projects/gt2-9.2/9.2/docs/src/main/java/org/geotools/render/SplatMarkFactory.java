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
package org.geotools.render;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.geotools.renderer.style.MarkFactory;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

class SplatMarkFactory implements MarkFactory {

private static GeneralPath SPLAT;

static {
    SPLAT = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    SPLAT.moveTo(-0.2f, 0.9f);
    SPLAT.lineTo(0.266f, -0.5f);
    SPLAT.lineTo(-0.366f, -0.7f);
    SPLAT.lineTo(0.4f, 1.12f);
    SPLAT.lineTo(0.3f, 1.10f);
}

public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature) throws Exception {
    if (symbolUrl == null) {
        // cannot handle a null url
        return null;
    }
    // Evaluate the expression as a String
    String wellKnownName = symbolUrl.evaluate(feature, String.class);
    
    if (wellKnownName != null && wellKnownName.equalsIgnoreCase("splat")) {
        return SPLAT;
    }
    return null; // we do not know this one
}
}