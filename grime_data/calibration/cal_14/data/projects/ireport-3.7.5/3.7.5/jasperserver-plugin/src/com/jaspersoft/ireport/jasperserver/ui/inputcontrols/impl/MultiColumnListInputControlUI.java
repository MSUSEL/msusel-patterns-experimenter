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

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;


/**
 *
 * @author gtoffoli
 */
public class MultiColumnListInputControlUI extends BasicInputControlUI {
    
    /**
     * Creates a new instance of ListInputControlUI
     */
    public MultiColumnListInputControlUI() {
        super();
        setComboEditable(false);
        getJComboBoxValue().setMinimumSize(new java.awt.Dimension(400,26));
        //getJComboBoxValue().setPreferredSize(new java.awt.Dimension(400,30));
    }
    
    public void setHistory(java.util.List values){
        
        getJComboBoxValue().removeAllItems();
        if (values == null) return;
        
        // Try to understand how much columns...
        int maxColumns = 1;
        for (int i=0; i<values.size(); ++i)
        {
            InputControlQueryDataRow qd =  (InputControlQueryDataRow)values.get(i);
            maxColumns = (qd.getColumnValues().size()>maxColumns) ? qd.getColumnValues().size() : maxColumns;
        }
        //System.out.println("ItemRenderer set to" + maxColumns );
        getJComboBoxValue().setRenderer(new ItemRenderer(maxColumns));
        
        for (int i=0; i<values.size(); ++i)
        {
            getJComboBoxValue().addItem( values.get(i));
        }
        
        if (getJComboBoxValue().getItemCount() > 0)
        {
            getJComboBoxValue().setSelectedIndex(0);
        }
        
        getJComboBoxValue().updateUI();
    }
    
    public void setValue(Object v)
    {
        for (int i=0; i<getJComboBoxValue().getItemCount(); ++i)
        {
            Object val = getJComboBoxValue().getItemAt(i);
            
            if (val instanceof InputControlQueryDataRow)
            {
                val = ((InputControlQueryDataRow)val).getValue();
                if ( ((val == null) ? val == v : val.equals(v)) )
                {
                    getJComboBoxValue().setSelectedIndex(i);
                    return;
                }
            }
        }
        
        getJComboBoxValue().setSelectedItem(v);
    }
     
     public Object getValue()
    {
        Object val = getJComboBoxValue().getSelectedItem();
        if (val == null) return null;
        if (val instanceof InputControlQueryDataRow)
        {
            return ((InputControlQueryDataRow)val).getValue();
        }
        
        return val;
    }
     
     
     
}
