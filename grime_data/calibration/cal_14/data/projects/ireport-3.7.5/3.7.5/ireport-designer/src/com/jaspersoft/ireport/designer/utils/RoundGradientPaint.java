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
package com.jaspersoft.ireport.designer.utils;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.ColorModel;
 
/**
 * @author gtoffoli
 */
public class RoundGradientPaint
    implements Paint {
  protected Point2D mPoint;
  protected Point2D mRadius;
  protected Color mPointColor, mBackgroundColor;
  
  public RoundGradientPaint(double x, double y, Color pointColor,
      Point2D radius, Color backgroundColor) {
    if (radius.distance(0, 0) <= 0)
      throw new IllegalArgumentException("Radius must be greater than 0.");
    mPoint = new Point2D.Double(x, y);
    mPointColor = pointColor;
    mRadius = radius;
    mBackgroundColor = backgroundColor;
  }
  
  public PaintContext createContext(ColorModel cm,
      Rectangle deviceBounds, Rectangle2D userBounds,
      AffineTransform xform, RenderingHints hints) {
    Point2D transformedPoint = xform.transform(mPoint, null);
    Point2D transformedRadius = xform.deltaTransform(mRadius, null);
    return new RoundGradientContext(transformedPoint, mPointColor,
        transformedRadius, mBackgroundColor);
  }
  
  public int getTransparency() {
    int a1 = mPointColor.getAlpha();
    int a2 = mBackgroundColor.getAlpha();
    return (((a1 & a2) == 0xff) ? OPAQUE : TRANSLUCENT);
  }
}


