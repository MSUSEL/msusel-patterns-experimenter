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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.swt.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.geotools.data.Parameter;
import org.geotools.util.Converters;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Field that uses the converter API to hack away at a text representation of the provided value.
 *
 *
 *
 *
 * @source $URL$
 */
public class JField extends ParamField {
    private Text text;
    private boolean single = true;

    public JField( Composite parent, Parameter< ? > parameter ) {
        super(parent, parameter);
    }

    public void setSingleLine( boolean single ) {
        this.single = single;
    }

    public Control doLayout() {
        if (parameter.metadata != null && parameter.metadata.get(Parameter.IS_PASSWORD) == Boolean.TRUE) {
            text = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.PASSWORD | SWT.BORDER);
            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        } else if (single) {
            text = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        } else {
            text = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        }
        text.addModifyListener(new ModifyListener(){
            public void modifyText( ModifyEvent arg0 ) {
                validate();
            }
        });
        return text;
    }

    public Object getValue() {
        String txt = text.getText();
        if (txt.length() == 0) {
            return null;
        }
        Object value = Converters.convert(txt, parameter.type);
        return value;
    }

    /**
     * Determine the number of dimensions based on the CRS metadata.
     * 
     * @return Number of dimensions expected based on metadata, default of 2
     */
    int getD() {
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) parameter.metadata.get(Parameter.CRS);
        if (crs == null) {
            return 2;
        } else {
            return crs.getCoordinateSystem().getDimension();
        }
    }

    public void setValue( Object value ) {
        String txt = (String) Converters.convert(value, String.class);
        if (txt == null) {
            txt = "";
        }
        text.setText(txt);
    }

    public boolean validate() {
        String txt = text.getText();
        if (txt.length() == 0) {
            return true;
        }
        Object value = Converters.convert(txt, parameter.type);
        if (value == null) {
            text.setToolTipText("Could not create " + parameter.type);
            text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
            return false;
        } else {
            text.setToolTipText(null);
            text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
            return true;
        }
    }

}
