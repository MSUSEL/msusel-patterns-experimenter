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

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.Stack;

/**
 *
 * @author gtoffoli
 */
public class Java2DUtils
{
  private static Stack clipBoundsStack = new Stack();
  private static Stack transforms = new Stack();

  public static void setClip(Graphics g, int x, int y, int width, int height)
  {
    setClip(g, new Rectangle(x, y, width, height));
  }

    @SuppressWarnings("unchecked")
  public static void setClip(Graphics g, Rectangle clipBounds)
  {
    Rectangle currentClipBounds;

    clipBounds = new Rectangle(clipBounds);
    clipBounds.width += 1;
    clipBounds.height += 1;

    currentClipBounds = g.getClipBounds();
    if(currentClipBounds != null)
    {
      clipBounds = clipBounds.intersection(g.getClipBounds());
    }

    clipBoundsStack.push(currentClipBounds);
    g.setClip(clipBounds);
  }

  public static void resetClip(Graphics g)
  {
    g.setClip((Shape) clipBoundsStack.pop());
  }
  
    @SuppressWarnings("unchecked")
    public static void setTransform(Graphics2D g2, AffineTransform transform)
  {
    AffineTransform current;


    current = g2.getTransform();
    transforms.push(current);
    g2.setTransform(transform);
  }


  public static void resetTransform(Graphics2D g2)
  {
    if(transforms.empty())
    {
      return;
    }


    g2.setTransform((AffineTransform) transforms.pop());
  }
  
  /**
   * This function provides a way to cancel the effects of the zoom on a particular Stroke.
   * All the stroke values (as line width, dashes, etc...) are divided by the zoom factor.
   * This allow to have essentially a fixed Stroke independent by the zoom.
   * The returned Stroke is a copy.
   * Remember to restore the right stroke in the graphics when done.
   * 
   * It works only with instances of BasicStroke
   * 
   * zoom is the zoom factor.
   */
  public static Stroke getInvertedZoomedStroke(Stroke stroke, double zoom)
  {
            if (stroke == null || !(stroke instanceof BasicStroke )) return stroke;
            
            BasicStroke bs = (BasicStroke)stroke;
            float[] dashArray = bs.getDashArray();
            
            float[] newDashArray = null;
            if (dashArray != null)
            {
                newDashArray = new float[dashArray.length];
                for (int i=0; i<newDashArray.length; ++i)
                {
                    newDashArray[i] = (float)(dashArray[i] / zoom);
                }
            }
            
            BasicStroke newStroke = new BasicStroke(       
                            (float)(bs.getLineWidth() / zoom),
                            bs.getEndCap(),
                            bs.getLineJoin(),
                            bs.getMiterLimit(),
                            //(float)(bs.getMiterLimit() / zoom),
                            newDashArray,
                            (float)(bs.getDashPhase() / zoom)
                    );
            return newStroke;
  }
}
