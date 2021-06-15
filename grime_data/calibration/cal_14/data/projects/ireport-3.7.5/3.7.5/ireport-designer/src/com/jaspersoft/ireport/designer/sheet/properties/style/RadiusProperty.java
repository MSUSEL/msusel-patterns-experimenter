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
package com.jaspersoft.ireport.designer.sheet.properties.style;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.lang.reflect.InvocationTargetException;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;

/**
 * Class to manage the JRDesignElement.PROPERTY_X property
 */
public final class RadiusProperty extends PropertySupport {

    private final JRBaseStyle style;

    @SuppressWarnings(value = "unchecked")
    public RadiusProperty(JRBaseStyle style) {
        super(JRBaseStyle.PROPERTY_RADIUS, Integer.class, I18n.getString("RadiusPropertyRadius.Property.Radius"), I18n.getString("RadiusPropertyRadius.Property.Radiusdetail"), true, true);
        this.style = style;
    }

    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return style.getRadius();
    }

    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (val == null || val instanceof Integer) {
            Integer oldValue = style.getOwnRadius();
            Integer newValue = (Integer) val;
            if (newValue != null && newValue < 0) {
                IllegalArgumentException iae = annotateException(I18n.getString("RadiusPropertyRadius.Property.Message"));
                throw iae;
            }

            style.setRadius(newValue);
            ObjectPropertyUndoableEdit urob = new ObjectPropertyUndoableEdit(style, "Radius", Integer.TYPE, oldValue, newValue);
            IReportManager.getInstance().addUndoableEdit(urob);
        }
    }

    public IllegalArgumentException annotateException(String msg) {
        IllegalArgumentException iae = new IllegalArgumentException(msg);
        ErrorManager.getDefault().annotate(iae, ErrorManager.EXCEPTION, msg, msg, null, null);
        return iae;
    }

    @Override
    public boolean isDefaultValue() {
        return style.getOwnRadius() == null;
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        setValue(null);
    }

    @Override
    public boolean supportsDefaultValue() {
        return true;
    }
}
