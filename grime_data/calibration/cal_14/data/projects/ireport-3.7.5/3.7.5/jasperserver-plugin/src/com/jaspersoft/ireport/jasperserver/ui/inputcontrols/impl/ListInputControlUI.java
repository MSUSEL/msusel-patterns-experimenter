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

import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.ListItemWrapper;




/**
 *
 * @author gtoffoli
 */
public class ListInputControlUI extends BasicInputControlUI {
    
    /**
     * Creates a new instance of ListInputControlUI
     */
    public ListInputControlUI() {
        setComboEditable(false);
    }
    
     public void setValue(Object v)
    {
        for (int i=0; i<getJComboBoxValue().getItemCount(); ++i)
        {
            Object val = getJComboBoxValue().getItemAt(i);
            
            if (val instanceof ListItemWrapper)
            {
                val = ((ListItemWrapper)val).getItem().getValue();
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
        if (val instanceof ListItemWrapper)
        {
            return ((ListItemWrapper)val).getItem().getValue();
        }
        
        return val;
    }
}
