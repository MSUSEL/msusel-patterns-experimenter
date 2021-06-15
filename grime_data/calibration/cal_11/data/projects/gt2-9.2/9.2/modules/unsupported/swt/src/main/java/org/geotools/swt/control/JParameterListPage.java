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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.geotools.data.Parameter;
import org.geotools.util.Converters;

/**
 * Parameters wizard page for trhe {@link JParameterListWizard parameter list wizard}. 
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class JParameterListPage extends WizardPage {
    public static final String ID = "org.geotools.swt.data.ParameterListPage";

    List<Parameter< ? >> contents;

    /** Map of user interface ParamFields displayed to the user */
    private Map<Parameter< ? >, ParamField> fields = new HashMap<Parameter< ? >, ParamField>();

    /** Connection params for datastore */
    protected Map<String, Object> connectionParameters;

    public JParameterListPage( String title, String description, List<Parameter< ? >> contents, Map<String, Object> params ) {
        super(ID);
        this.contents = contents;
        this.connectionParameters = params;

        setTitle(title);
        setDescription(description);
    }

    public void createControl( Composite parent ) {
        Composite mainComposite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        mainComposite.setLayout(gridLayout);

        for( Parameter< ? > param : contents ) {
            String txt = param.title.toString();
            if (param.required) {
                txt += "*";
            }

            Label label = new Label(mainComposite, SWT.NONE);
            label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
            label.setText(txt);

            ParamField field = ParamField.create(mainComposite, param);
            field.doLayout();

            fields.put(param, field);

            // if (param.description != null) {
            // JLabel info = new JLabel("<html>" + param.description.toString());
            // page.add(info, "skip, span, wrap");
            // }
        }

        setControl(mainComposite);
    }

    public void setVisible( boolean visible ) {
        if (visible) {
            preDisplayPanel();
        } else {
            preClosePanel();
        }

        super.setVisible(visible);
    }

    private void preDisplayPanel() {
        // populate panel from params map
        for( Entry<Parameter< ? >, ParamField> entry : fields.entrySet() ) {
            Parameter< ? > param = entry.getKey();
            ParamField field = entry.getValue();
            Object value = null;
            Object object = connectionParameters.get(param.key);
            value = Converters.convert(object, param.type);
            if (value == null) {
                value = object;
            }
            if (value == null && param.required) {
                value = param.sample;
            }
            field.setValue(value);
        }
        // for (Entry<Parameter<?>, ParamField> entry : fields.entrySet()) {
        // ParamField field = entry.getValue();
        // field.addListener(getJWizard().getController());
        // }
    }

    private void preClosePanel() {
        for( Entry<Parameter< ? >, ParamField> entry : fields.entrySet() ) {
            Parameter< ? > param = entry.getKey();
            ParamField field = entry.getValue();

            Object value = field.getValue();
            connectionParameters.put(param.key, value);
            // field.setValue(value);
        }
        // for (Entry<Parameter<?>, ParamField> entry : fields.entrySet()) {
        // ParamField field = entry.getValue();
        // field.removeListener(getJWizard().getController());
        // }
    }

    public boolean isValid() {
        // populate panel
        for( Entry<Parameter< ? >, ParamField> entry : fields.entrySet() ) {
            Parameter< ? > param = entry.getKey();
            ParamField field = entry.getValue();

            if (!field.validate()) {
                return false; // not validate
            }
            if (param.required && field.getValue() == null) {
                return false; // a value is required here
            }
        }
        return true;
    }

}
