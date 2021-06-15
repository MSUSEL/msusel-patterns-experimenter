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
package com.jaspersoft.ireport.designer.charts;
import java.awt.*;
import javax.swing.*;

public class ScrollableList extends JList
{
   protected boolean trackWidth = true;
   protected boolean trackHeight = false;

   public ScrollableList()
   {
      super();
      setCellRenderer(new ChartCellRenderer());
   }
   
   public int getVisibleRowCount()
   {
      return 0;
   }
   
   public boolean getScrollableTracksViewportWidth()
   {
      return trackWidth;
   }

   public void setScrollableTracksViewportWidth(
      boolean trackWidth)
   {
      this.trackWidth = trackWidth;
   }

   public boolean getScrollableTracksViewportHeight()
   {
      return trackHeight;
   }

   public void setScrollableTracksViewportHeight(
      boolean trackHeight)
   {
      this.trackHeight = trackHeight;
   }
   
   public void setLayoutOrientation(
      int orientation)
   {
      super.setLayoutOrientation(orientation);
      if (orientation == VERTICAL)
      {
         setScrollableTracksViewportWidth(true);
         setScrollableTracksViewportHeight(false);
      }
      if (orientation == VERTICAL_WRAP)
      {
         setScrollableTracksViewportWidth(false);
         setScrollableTracksViewportHeight(true);
      }
      if (orientation == HORIZONTAL_WRAP)
      {
         setScrollableTracksViewportWidth(true);
         setScrollableTracksViewportHeight(false);
      }
      revalidate();
   }
}
