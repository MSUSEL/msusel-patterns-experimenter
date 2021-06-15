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
package com.jaspersoft.ireport.designer.toolbars;

import java.awt.Dimension;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @version $Id: WideComboBox.java 0 2009-10-23 12:48:57 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class WideComboBox extends JComboBox {

    public WideComboBox()
    {
        super();
        setup();
    }

    public WideComboBox(ComboBoxModel aModel)
    {
        super(aModel);
        setup();
    }


    private void setup()
    {
        
        this.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                JComboBox box = WideComboBox.this;
                Object comp = box.getUI().getAccessibleChild(box, 0);
                if (!(comp instanceof JPopupMenu)) return; // Not on a standard look and feel.

                JPopupMenu menu = (JPopupMenu)comp;

                for (int i=0; i<menu.getComponentCount(); ++i)
                {
                    if (menu.getComponent(i) instanceof JScrollPane)
                    {
                        JScrollPane scroller = (JScrollPane)menu.getComponent(i);
                        Dimension scrollSize = new Dimension(250, scroller.getPreferredSize().height);
                        scroller.setMaximumSize(scrollSize);
                        scroller.setPreferredSize(scrollSize);
                        scroller.setMinimumSize(scrollSize);

                        break;
                    }
                }
                //menu.setBounds(menu.getBounds().x, menu.getBounds().y, 300, menu.getPreferredSize().height);

                menu.updateUI();

                /*
                menu.setPreferredSize(new Dimension(300, ));
                menu.doLayout();
                */
                /*
               
                Dimension size = new Dimension();
                size.width = 300; //box.getPreferredSize().width;
                size.height = scrollPane.getPreferredSize().height;
                System.out.println("Menu size: " + size);
                System.out.flush();
                scrollPane.setPreferredSize(size);
                scrollPane.setSize(size);
                scrollPane.updateUI();
                */
             }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                
                
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        
    }


}


