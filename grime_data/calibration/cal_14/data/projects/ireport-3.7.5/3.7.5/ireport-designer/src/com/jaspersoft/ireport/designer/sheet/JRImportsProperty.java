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

import com.jaspersoft.ireport.designer.sheet.editors.JRImportsPropertyEditor;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author gtoffoli
 */
public class JRImportsProperty  extends PropertySupport {

    PropertyEditor editor = null;
    JasperDesign jd = null;
    
    @SuppressWarnings("unchecked")
    public JRImportsProperty(JasperDesign jd)
    {
       super( "imports", (new String[0]).getClass(), I18n.getString("Property.Imports"),I18n.getString("Property.Imports.detail"), true,true);
       setValue("canEditAsText", Boolean.FALSE);
       this.jd = jd;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return jd.getImports();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!(val != null && val.getClass().isArray() && val.getClass().getComponentType().equals(String.class) )) throw new IllegalArgumentException();

        if (jd.getImports() == val) return;

        // Fill this map with the content of the map we got here...
        String[] originalImports = jd.getImports();
        for (int i=0; originalImports != null && i<originalImports.length; ++i )
        {
            jd.removeImport(originalImports[i]);
        }

        String[] newImports = (String[])val;
        for (int i=0; newImports != null && i<newImports.length; ++i )
        {
            jd.addImport(newImports[i]);
        }


        com.jaspersoft.ireport.designer.IReportManager.getInstance().notifyReportChange();
    }
    
    @Override
    public PropertyEditor getPropertyEditor() {
        
        if (editor == null)
        {
            editor = new JRImportsPropertyEditor();
        }
        return editor;
    }
    
    
}

