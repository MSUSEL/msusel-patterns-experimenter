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
package com.jaspersoft.ireport.designer.sheet.editors;

import com.jaspersoft.ireport.designer.sheet.*;
import java.beans.FeatureDescriptor;
import java.beans.PropertyEditorSupport;
import java.util.List;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.nodes.Node;


public class ComboBoxPropertyEditor extends PropertyEditorSupport
        implements ExPropertyEditor { //, InplaceEditor.Factory
    
    private java.util.List tagValues = null;
    private boolean editable = true;
            
    /** Creates a new instance of DataPropertyEditor 
     *  @param boolean editable
     *  @param java.util.List tagValues
     */
    public ComboBoxPropertyEditor(boolean editable, java.util.List tagValues) {
        
        super();
        this.tagValues = tagValues;
        this.editable = editable;
    }

    @Override
    public void setValue(Object value) {

        Tag t = Tag.findTagByValue(value, tagValues);
        super.setValue( t != null ? t : value);
    }

    @Override
    public Object getValue() {

        Object value = super.getValue();
        if (value instanceof Tag)
        {
            return ((Tag)value).getValue();
        }

        return value;
    }
    
    @java.lang.Override
    public String getAsText() {
        Object key = getValue();
        
        // Look for the right tag...
        Tag t = Tag.findTagByValue(key, tagValues);
        if (t == null) {
            return ""+key;
        }
        return t.toString();
    }
    
    @java.lang.Override
    public void setAsText(String s) {
        Tag t = Tag.findTagByName(s, tagValues);
        setValue( t != null ? t.getValue() : s);
    }
    
    private String instructions=null;
    private boolean oneline=false;
    private boolean customEd=true;
    private PropertyEnv env;
    
    
    public void attachEnv(PropertyEnv env) {
        this.env = env;       
        FeatureDescriptor desc = env.getFeatureDescriptor();
        if (desc instanceof Node.Property){
            Node.Property prop = (Node.Property)desc;
            instructions = (String) prop.getValue ("instructions"); //NOI18N
            oneline = Boolean.TRUE.equals (prop.getValue ("oneline")); //NOI18N
            customEd = !Boolean.TRUE.equals (prop.getValue("suppressCustomEditor")); //NOI18N
        }
    }

    @Override
    public String[] getTags() {
        
        String[] s = new String[tagValues.size()];
        for (int i=0; i<tagValues.size(); ++i)
        {
            s[i] = ""+tagValues.get(i);
        }
        return s;
    }
    
    public void setTagValues(List newValues)
    {
        this.tagValues = newValues;
        this.firePropertyChange();
    }


    @Override
    public boolean supportsCustomEditor () {
        return customEd;
    }

    
    public java.awt.Component getCustomEditor () {
        Object val = getValue();
        String s = ""; // NOI18N
        if (val != null) {
            s = val instanceof String ? (String) val : val.toString();
        }
        return new StringCustomEditor(s, editable, oneline, instructions, this, env); // NOI18N
    }
    
    
    
}

