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

import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.ListInputControlUI;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.RadioListInputControlUI;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;

/**
 *
 * @author gtoffoli
 */
public class SingleSelectInputControl extends BasicInputControl{
    
    java.util.List wrappedItems = null;
    
    /** Creates a new instance of BooleanInputControl */
    public SingleSelectInputControl() {
        super();
    }
    
     public void setInputControl(ResourceDescriptor inputControl, List items) {
        
         this.inputControl = inputControl;
         
         String defaultNullLabel = "";
         
         if (inputControl.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES)
         {

            if (getInputControlUI() == null || !(getInputControlUI() instanceof ListInputControlUI))
            {
                setInputControlUI( new ListInputControlUI());
            }
         }
         else if (inputControl.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_QUERY_RADIO ||
             inputControl.getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO)
         {

            if (getInputControlUI() == null || !(getInputControlUI() instanceof RadioListInputControlUI))
            {
                setInputControlUI( new RadioListInputControlUI());
                defaultNullLabel = "-None-";
            }

            if (getInputControlUI() instanceof RadioListInputControlUI)
            {
                defaultNullLabel = "-None-";
            }
         }
         
        String label = inputControl.getLabel() + ((inputControl.isMandatory()) ? "*" : "");
        getInputControlUI().setLabel(label);
        
        
        wrappedItems = new java.util.ArrayList();
        
        if (!inputControl.isMandatory())
        {
            wrappedItems.add(new ListItemWrapper(new ListItem(defaultNullLabel,null)));
        }
        
        for (int i=0; items != null && items.size()>i; ++i)
        {
            Object itemObject = items.get(i);
            if (itemObject instanceof ListItem)
            {
                ListItem item = (ListItem)itemObject;
                wrappedItems.add( new ListItemWrapper(item));
            }
            else if (itemObject instanceof InputControlQueryDataRow)
            {
                InputControlQueryDataRow qd =  (InputControlQueryDataRow)itemObject;
            
                List itemColumnValues = qd.getColumnValues();
                String text = "";
            
                for (int k=0; k<itemColumnValues.size(); ++k)
                {
                   if (k>0) text += " | ";
                   text += itemColumnValues.get(k);
                }
                
                wrappedItems.add( new ListItemWrapper(new ListItem(text, qd.getValue())));
            }
           
        }
        
        getInputControlUI().setHistory( wrappedItems );
        getInputControlUI().setReadOnly( inputControl.isReadOnly() );
    
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
