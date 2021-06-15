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
package com.jaspersoft.ireport.designer.sheet;

import com.jaspersoft.ireport.designer.sheet.editors.JRPenPropertyEditor;
import com.jaspersoft.ireport.designer.sheet.properties.AbstractProperty;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPenContainer;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.type.LineStyleEnum;

/**
 *
 * @author gtoffoli
 */
public class JRPenProperty extends AbstractProperty {

    JRPen pen = null;
    JRPenPropertyEditor editor = null;
    JRPenContainer container = null;
    
    @SuppressWarnings("unchecked")
    public JRPenProperty(JRPen pen, JRPenContainer container)
    {
       super(JRPen.class, pen);
       setName("pen");
       setDisplayName(I18n.getString("PenProperty.Property.Pen"));        
       setShortDescription(I18n.getString("JRPenProperty.Property.detail"));
       setValue( "canEditAsText", Boolean.FALSE );
       this.pen = pen;
       this.container = container;
    }

    public void setPen(JRPen mpen)
    {
        if (mpen != null)
        {
            pen.setLineColor( mpen.getOwnLineColor());
            pen.setLineWidth( mpen.getOwnLineWidth());
            pen.setLineStyle( mpen.getOwnLineStyle());
        }
        else
        {
            pen.setLineColor( null );
            pen.setLineWidth( null );
            pen.setLineStyle( (LineStyleEnum)null );
        }
    }
    
    @Override
    public boolean isDefaultValue() {
        
        if (pen == null) return true;
        
        if (pen.getOwnLineColor() != null) return false;
        if (pen.getOwnLineWidth() != null) return false;
        if (pen.getOwnLineStyle() != null) return false;
        
        return true;
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }
    
    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRPenPropertyEditor();
        }
        return editor;
    }

    @Override
    public Object getPropertyValue() {
        return pen.clone(container);
    }

    @Override
    public Object getOwnPropertyValue() {
        return pen.clone(container);
    }

    @Override
    public Object getDefaultValue() {
        return new JRBasePen(null);//FIXME this is dangerous. check it
    }

    @Override
    public void validate(Object value) {
        
    }

    @Override
    public void setPropertyValue(Object value) {
        
        if (value instanceof JRPen)
        {
            setPen((JRPen)value);
        }
        else
        {
           setPen(null);
        }
    }
    
}
