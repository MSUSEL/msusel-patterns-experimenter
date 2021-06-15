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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;

/**
 * Data chooser wizard page for the {@link JDataStoreWizard data store wizard}.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class JDataChoosePage extends WizardPage implements ISelectionChangedListener {
    public static final String ID = "org.geotools.swt.data.DataChoosePage";
    private DataStoreFactorySpi selectedFactory;
    private boolean canFlip;

    public JDataChoosePage() {
        super(ID);
        setTitle("Choose DataStore");
        setDescription("Available DataStores on your classpath");
    }

    public void createControl( Composite parent ) {
        Composite mainComposite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        mainComposite.setLayout(gridLayout);

        List<DataStoreFactorySpi> factoryList = new ArrayList<DataStoreFactorySpi>();
        for( Iterator<DataStoreFactorySpi> iter = DataStoreFinder.getAvailableDataStores(); iter.hasNext(); ) {
            factoryList.add(iter.next());
        }

        TableViewer viewer = new TableViewer(mainComposite);
        GridData viewerGD = new GridData(SWT.FILL, SWT.FILL, true, false);
        viewer.getTable().setLayoutData(viewerGD);
        viewer.addSelectionChangedListener(this);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new LabelProvider(){
            public String getText( Object element ) {
                if (element instanceof DataStoreFactorySpi) {
                    DataStoreFactorySpi factory = (DataStoreFactorySpi) element;
                    return factory.getDisplayName();
                }
                return super.getText(element);
            }
        });

        viewer.setInput(factoryList.toArray());

        setControl(mainComposite);

        canFlip = false;
    }

    public boolean canFlipToNextPage() {
        return canFlip;
    }

    public DataStoreFactorySpi getSelectedFactory() {
        return selectedFactory;
    }

    public void selectionChanged( SelectionChangedEvent event ) {
        ISelection selection = event.getSelection();
        if (selection instanceof StructuredSelection) {
            StructuredSelection sel = (StructuredSelection) selection;
            Object selObj = sel.getFirstElement();
            if (selObj instanceof DataStoreFactorySpi) {
                selectedFactory = (DataStoreFactorySpi) selObj;
            }
        }
        if (selectedFactory != null) {
            canFlip = true;
        }
        getWizard().getContainer().updateButtons();
    }
}
