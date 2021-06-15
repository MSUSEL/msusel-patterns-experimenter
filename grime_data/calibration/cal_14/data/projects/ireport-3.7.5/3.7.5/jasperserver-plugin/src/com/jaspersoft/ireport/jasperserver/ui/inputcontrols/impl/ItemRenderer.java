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
package com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl;

import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 *
 * @author gtoffoli
 */
public // The combobox's renderer...
	class ItemRenderer extends JPanel implements ListCellRenderer
	{
		private JLabel[]	labels = null;
		//private JLabel		nameLabel = new JLabel(" ");
		//private JLabel		valueLabel = new JLabel(" ");
		int columns = 0;
 
		public ItemRenderer(int columns)
		{
			//setLayout(new GridBagLayout());
                        GridLayout g = new GridLayout(1,columns);
                        setLayout(g);

                        this.columns = columns;
 			labels = new JLabel[columns];
                        
                        //java.awt.GridBagConstraints gridBagConstraints = null;
                                
 			for (int i=0; i<columns; ++i)
 			{
 			   labels[i] = new JLabel(" ");
                           //gridBagConstraints = new java.awt.GridBagConstraints();
                           //gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                           //gridBagConstraints.weightx = 1.0;
                           //gridBagConstraints.weighty = 1.0;
                           
 			   add(labels[i]); //, gridBagConstraints);
 			}
 		}
 
 
		public Component getListCellRendererComponent(
							JList list,
							Object value,
							int index,
							boolean isSelected,
							boolean cellHasFocus )
		{
                    
                        if (value != null && value instanceof InputControlQueryDataRow)
                        {
                            InputControlQueryDataRow icqdr = (InputControlQueryDataRow)value;
                            if (value != null)
                            {
                                for (int i=0; i<this.columns; ++i)
                                {
                                    String s = " ";
                                    try {
                                       if (icqdr.getColumnValues().get(i) != null)
                                       {
                                           s = ""+icqdr.getColumnValues().get(i);
                                           
                                       }
                                    } catch (Exception ex) { }
                                    
                                    getLabels()[i].setText( s );
                                
                                }
                                this.updateUI();
                            }
                            
                        }
                        else
                        {

                            getLabels()[0].setText(Misc.nvl(value, " "));
                            
                            for (int i=1; i<this.columns; ++i)
                            {
                                getLabels()[i].setText(" ");
                            }
                        }
                     

                        setOpaque(isSelected);
                        
                        if (!isSelected)
                        {
                            Color bg = UIManager.getColor("List.background");
                            if (bg != null) this.setBackground(bg);
                            Color fg = UIManager.getColor("List.foreground");
                            for (int i=0; i<this.columns; ++i)
                            {
                                getLabels()[i].setForeground(fg);
                                getLabels()[i].setBackground(bg);
                            }
                        }
                        else
                        {
                            Color bg = UIManager.getColor("List.selectionBackground");
                            if (bg != null) this.setBackground(bg);
                            Color fg = UIManager.getColor("List.selectionForeground");
                            for (int i=0; i<this.columns; ++i)
                            {
                                getLabels()[i].setForeground(fg);
                                getLabels()[i].setBackground(bg);
                            }
                        }

                        for (int i=0; i<this.columns; ++i)
                        {
                            getLabels()[i].setOpaque(isSelected);
                        }

			return this;
		}

    public JLabel[] getLabels() {
        return labels;
    }

    public void setLabels(JLabel[] labels) {
        this.labels = labels;
    }
	}
