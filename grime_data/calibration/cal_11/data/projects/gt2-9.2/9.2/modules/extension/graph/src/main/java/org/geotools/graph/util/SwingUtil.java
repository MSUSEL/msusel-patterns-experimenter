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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 *
 * @source $URL$
 */
public class SwingUtil {
  
  public static ListModel toListModel(final List elements) {
    return(
      new AbstractListModel() {
        public int getSize() {
          return(elements.size());
        }

        public Object getElementAt(int index) {
          return(elements.get(index));
        }
      }
    );
  }
  
  public static ListModel toListModel(Collection elements) {
    return(toListModel(new ArrayList(elements)));  
  }
  
  public static List toList(ListModel model) {
    ArrayList list = new ArrayList(model.getSize());
    for (int i = 0; i < model.getSize(); i++) {
      list.add(model.getElementAt(i));  
    }
    
    return(list);
  }
  
  public static void setSelection(JList list, Object element) {
    for (int i = 0; i < list.getModel().getSize(); i++) {
      Object value = (Object)list.getModel().getElementAt(i);
      if (value == element) {
        list.setSelectedIndex(i);
        list.scrollRectToVisible(
          list.getCellBounds(i,i)  
        ); 
        return;
      }
    }
  }
  
  public static void addDoubleClickEvent(JList list) {
    list.addMouseListener(
      new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          JList source = (JList)e.getSource();
          if (e.getClickCount() == 2) {
            ListSelectionListener[] listeners = source.getListSelectionListeners();
            for (int i = 0; i < listeners.length; i++) {
              listeners[i].valueChanged(
                new ListSelectionEvent(
                  source, source.getSelectedIndex(),
                  source.getSelectedIndex(), false
                )
              );
            }   
          }
        }
      }  
    );
  }
}
