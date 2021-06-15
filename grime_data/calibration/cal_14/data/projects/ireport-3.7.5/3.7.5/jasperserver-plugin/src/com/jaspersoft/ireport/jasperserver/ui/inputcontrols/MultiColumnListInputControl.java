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
package com.jaspersoft.ireport.jasperserver.ui.inputcontrols;

import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.MultiColumnListInputControlUI;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;

/**
 *
 * @author gtoffoli
 */
public class MultiColumnListInputControl extends BasicInputControl{
    
    java.util.List wrappedItems = null;
    
    /** Creates a new instance of BooleanInputControl */
    public MultiColumnListInputControl() {
        super();
        setInputControlUI( new MultiColumnListInputControlUI());
    }
    
     public void setInputControl(ResourceDescriptor inputControl, List items) {
        
         this.inputControl = inputControl;
         
        String label = inputControl.getLabel() + ((inputControl.isMandatory()) ? "*" : "");
        getInputControlUI().setLabel(label);
        getInputControlUI().setReadOnly( inputControl.isReadOnly() );
        
        wrappedItems = new java.util.ArrayList();
        
        if (!inputControl.isMandatory())
        {
            InputControlQueryDataRow icqdr = new InputControlQueryDataRow();
            icqdr.setValue(null);
            wrappedItems.add(icqdr);
        }
        
        for (int i=0; i < items.size(); ++i)
        {
            InputControlQueryDataRow item = (InputControlQueryDataRow)items.get(i);
            wrappedItems.add( item);
        }

        getInputControlUI().setHistory( wrappedItems );
    
        List history = getHistory(inputControl.getUriString());
     
        if (history != null && history.size() > 0)
        {
            getInputControlUI().setValue( history.get(0) );
        }
     }
     
     public Object validate() throws InputValidationException
     {
        return getInputControlUI().getValue();
     }
     
}
