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
import java.awt.image.*;
 
/**
 *
 * @author gtoffoli
 */
public class RoundGradientContext
    implements PaintContext {
  protected Point2D mPoint;
  protected Point2D mRadius;
  protected Color mC1, mC2;
  public RoundGradientContext(Point2D p, Color c1, Point2D r, Color c2) {
    mPoint = p;
    mC1 = c1;
    mRadius = r;
    mC2 = c2;
  }
  
  public void dispose() {}
  
  public ColorModel getColorModel() { return ColorModel.getRGBdefault(); }
  
  public Raster getRaster(int x, int y, int w, int h) {
    WritableRaster raster =
        getColorModel().createCompatibleWritableRaster(w, h);
    
    int[] data = new int[w * h * 4];
    for (int j = 0; j < h; j++) {
      for (int i = 0; i < w; i++) {
        double distance = mPoint.distance(x + i, y + j);
        double radius = mRadius.distance(0, 0);
        double ratio = distance / radius;
        if (ratio > 1.0)
          ratio = 1.0;
      
        int base = (j * w + i) * 4;
        data[base + 0] = (int)(mC1.getRed() + ratio *
            (mC2.getRed() - mC1.getRed()));
        data[base + 1] = (int)(mC1.getGreen() + ratio *
            (mC2.getGreen() - mC1.getGreen()));
        data[base + 2] = (int)(mC1.getBlue() + ratio *
            (mC2.getBlue() - mC1.getBlue()));
        data[base + 3] = (int)(mC1.getAlpha() + ratio *
            (mC2.getAlpha() - mC1.getAlpha()));
      }
    }
    raster.setPixels(0, 0, w, h, data);
    
    return raster;
  }
}
