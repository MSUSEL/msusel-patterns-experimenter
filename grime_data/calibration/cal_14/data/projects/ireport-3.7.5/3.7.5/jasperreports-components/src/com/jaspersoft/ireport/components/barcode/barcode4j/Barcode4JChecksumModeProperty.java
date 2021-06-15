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
package com.jaspersoft.ireport.components.barcode.barcode4j;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.sheet.editors.ComboBoxPropertyEditor;
import com.jaspersoft.ireport.designer.undo.ObjectPropertyUndoableEdit;
import com.jaspersoft.ireport.locale.I18n;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.EAN128Component;
import net.sf.jasperreports.components.barcode4j.EAN13Component;
import net.sf.jasperreports.components.barcode4j.EAN8Component;
import net.sf.jasperreports.components.barcode4j.FourStateBarcodeComponent;
import net.sf.jasperreports.components.barcode4j.Interleaved2Of5Component;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;
import net.sf.jasperreports.components.barcode4j.UPCAComponent;
import net.sf.jasperreports.components.barcode4j.UPCEComponent;
import org.openide.nodes.PropertySupport;


public final class Barcode4JChecksumModeProperty  extends PropertySupport
{
        private BarcodeComponent component;
        private ComboBoxPropertyEditor editor;

        @SuppressWarnings("unchecked")
        public Barcode4JChecksumModeProperty(BarcodeComponent component)
        {
            // TODO: Replace WhenNoDataType with the right constant
            super( Code39Component.PROPERTY_CHECKSUM_MODE,String.class,
                    I18n.getString("barcode4j.property.checksumMode.name"),
                    I18n.getString("barcode4j.property.checksumMode.description"), true, true);
            this.component = component;
            setValue("suppressCustomEditor", Boolean.TRUE);
        }

        @Override
        @SuppressWarnings("unchecked")
        public PropertyEditor getPropertyEditor() {

            if (editor == null)
            {

                editor = new ComboBoxPropertyEditor(false, getListOfTags());
            }
            return editor;
        }

        public Object getValue() throws IllegalAccessException, InvocationTargetException {

            return getChecksumMode();
        }

        public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (val == null || val instanceof String)
            {
                String oldValue = getChecksumMode();

                String newValue = (String)val;
                setChecksumMode(newValue);

                ObjectPropertyUndoableEdit urob =
                        new ObjectPropertyUndoableEdit(
                            component,
                            "ChecksumMode",
                            String.class,
                            oldValue,newValue);
                // Find the undoRedo manager...
                IReportManager.getInstance().addUndoableEdit(urob);
            }
        }

    private java.util.ArrayList getListOfTags()
    {
        ArrayList tags = new java.util.ArrayList();
        tags.add(new Tag(null, "<default>"));
        tags.add(new Tag("auto", "Auto"));
        tags.add(new Tag("ignore", "Ignore"));
        tags.add(new Tag("add", "Add"));
        tags.add(new Tag("check", "Check"));
        
        return tags;
    }

    private String getChecksumMode()
    {
        if (component instanceof EAN128Component)
        {
            return ((EAN128Component)component).getChecksumMode();
        }
        else if (component instanceof FourStateBarcodeComponent)
        {
            return ((FourStateBarcodeComponent)component).getChecksumMode();
        }
        else if (component instanceof Code39Component)
        {
            return ((Code39Component)component).getChecksumMode();
        }
        else if (component instanceof Interleaved2Of5Component)
        {
            return ((Interleaved2Of5Component)component).getChecksumMode();
        }
        else if (component instanceof UPCAComponent)
        {
            return ((UPCAComponent)component).getChecksumMode();
        }
        else if (component instanceof UPCEComponent)
        {
            return ((UPCEComponent)component).getChecksumMode();
        }
        else if (component instanceof EAN13Component)
        {
            return ((EAN13Component)component).getChecksumMode();
        }
        else if (component instanceof EAN8Component)
        {
            return ((EAN8Component)component).getChecksumMode();
        }
        else if (component instanceof POSTNETComponent)
        {
            return ((POSTNETComponent)component).getChecksumMode();
        }
        return null;
    }

    private void setChecksumMode(String s)
    {
        if (component instanceof EAN128Component)
        {
            ((EAN128Component)component).setChecksumMode(s);
        }
        else if (component instanceof FourStateBarcodeComponent)
        {
            ((FourStateBarcodeComponent)component).setChecksumMode(s);
        }
        else if (component instanceof Code39Component)
        {
            ((Code39Component)component).setChecksumMode(s);
        }
        else if (component instanceof Interleaved2Of5Component)
        {
            ((Interleaved2Of5Component)component).setChecksumMode(s);
        }
        else if (component instanceof UPCAComponent)
        {
            ((UPCAComponent)component).setChecksumMode(s);
        }
        else if (component instanceof UPCEComponent)
        {
            ((UPCEComponent)component).setChecksumMode(s);
        }
        else if (component instanceof EAN13Component)
        {
            ((EAN13Component)component).setChecksumMode(s);
        }
        else if (component instanceof EAN8Component)
        {
            ((EAN8Component)component).setChecksumMode(s);
        }
        else if (component instanceof POSTNETComponent)
        {
            ((POSTNETComponent)component).setChecksumMode(s);
        }
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
    public boolean isDefaultValue() {
        return getChecksumMode() == null;
    }
}

