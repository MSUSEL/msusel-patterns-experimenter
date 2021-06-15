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
import javax.swing.event.*;

public class JListView extends JPanel
   implements ChangeListener
{
   protected JScrollPane scroll;
   private ScrollableList list;
   
   int listLayout = JList.HORIZONTAL_WRAP;
   
   public JListView()
   {
      setLayout(new BorderLayout());
      
      add(BorderLayout.CENTER, scroll = 
         new JScrollPane(
         list = new ScrollableList() ));
      scroll.getViewport().setBackground(
         getList().getBackground());
      stateChanged(new ChangeEvent(this));
   }
   
   public void stateChanged(ChangeEvent event)
   {
      getList().setLayoutOrientation(listLayout);
      if (listLayout == JList.VERTICAL)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      }
      if (listLayout == JList.VERTICAL_WRAP)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_NEVER);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      }
      if (listLayout == JList.HORIZONTAL_WRAP)
      {
         scroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         scroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      }
      scroll.revalidate();
   }

    public ScrollableList getList() {
        return list;
    }

    public void setList(ScrollableList list) {
        this.list = list;
    }
   

}
