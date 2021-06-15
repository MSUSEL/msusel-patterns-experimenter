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
package com.jaspersoft.ireport.designer.fonts;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author gtoffoli
 */
public class CheckBoxList extends JList
{
    
   public CheckBoxList()
   {
      super();
      
      setModel( new DefaultListModel());
      setCellRenderer(new CheckboxCellRenderer());
      
      addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
               int index = locationToIndex(e.getPoint());

               if (index != -1) {
                  Object obj = getModel().getElementAt(index);
                  if (obj instanceof JCheckBox)
                  {
                    JCheckBox checkbox = (JCheckBox)obj;
                              
                    checkbox.setSelected(
                                     !checkbox.isSelected());
                    repaint();
                  }
               }
            }
         }
      
      );

      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }
   
   
    @SuppressWarnings("unchecked")
   public int[] getCheckedIdexes()
   {
       java.util.List list = new java.util.ArrayList();
       DefaultListModel dlm = (DefaultListModel)getModel();
       for (int i=0; i<dlm.size(); ++i)
       {
            Object obj = getModel().getElementAt(i);
            if (obj instanceof JCheckBox)
            {
                JCheckBox checkbox = (JCheckBox)obj;
                if (checkbox.isSelected())
                {
                    list.add(new Integer(i));
                }
            }
       }
       
       int[] indexes = new int[list.size()];
       
       for (int i=0; i<list.size(); ++i)
       {
            indexes[i] = ((Integer)list.get(i)).intValue();
       }
       
       return indexes;
   }
   
   
    @SuppressWarnings("unchecked")
   public java.util.List getCheckedItems()
   {
       java.util.List list = new java.util.ArrayList();
       DefaultListModel dlm = (DefaultListModel)getModel();
       for (int i=0; i<dlm.size(); ++i)
       {
            Object obj = getModel().getElementAt(i);
            if (obj instanceof JCheckBox)
            {
                JCheckBox checkbox = (JCheckBox)obj;
                if (checkbox.isSelected())
                {
                    list.add(checkbox);
                }
            }
       }
       return list;
   }
}

