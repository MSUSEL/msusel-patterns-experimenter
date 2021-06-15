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
package com.jaspersoft.ireport.designer.sheet.properties;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.nodes.PropertySupport;

/**
 * Class to manage the JRBaseFont.PROPERTY_REPORT_FONT property
 */
public class ReportFontProperty extends PropertySupport.ReadWrite {

    // FIXME: refactorize this
    private final JRDesignTextElement element;
    private final JasperDesign jd;
    PropertyEditor editor = null;

    @SuppressWarnings("unchecked")
    public ReportFontProperty(JRDesignTextElement element, JasperDesign jd)
    {
        super(JRBaseFont.PROPERTY_REPORT_FONT, JRReportFont.class,
              I18n.getString("Global.Property.Reportfont"),
              I18n.getString("Global.Property.Reportfont"));
        this.element = element;
        this.jd = jd;

        setValue("canEditAsText",false);
        setValue("oneline",true);
        setValue("suppressCustomEditor",true);
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return element.getReportFont() == null ? "" : element.getReportFont();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if (val == null || val instanceof JRReportFont)
        {
            JRReportFont oldValue = element.getReportFont();
            JRReportFont newValue =   (JRReportFont)val;
            element.setReportFont(newValue);

            ObjectPropertyUndoableEdit urob =
                    new ObjectPropertyUndoableEdit(
                        element,
                        "ReportFont", 
                        JRReportFont.class,
                        oldValue,newValue);
            // Find the undoRedo manager...
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    @Override
    public boolean isDefaultValue() {
        return element.getReportFont() == null;
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyEditor getPropertyEditor() {

        if (editor == null)
        {
            java.util.List classes = new ArrayList();
            @SuppressWarnings("deprecation")
            List fonts = jd.getFontsList();
            for (int i=0; i<fonts.size(); ++i)
            {
                JRReportFont font = (JRReportFont)fonts.get(i);
                classes.add(new Tag(font, font.getName() ));
            }

            editor = new ComboBoxPropertyEditor(false, classes);
        }
        return editor;
    }

    @Override
    public String getHtmlDisplayName() {
        return "<s>" + super.getDisplayName() + "</s>";
    }
    
}
