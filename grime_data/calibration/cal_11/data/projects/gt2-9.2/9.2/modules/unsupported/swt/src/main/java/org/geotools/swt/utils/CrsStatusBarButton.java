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
package org.geotools.swt.utils;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.swt.SwtMapPane;
import org.geotools.swt.control.CRSChooserDialog;
import org.geotools.swt.event.MapPaneAdapter;
import org.geotools.swt.event.MapPaneEvent;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The {@link CoordinateReferenceSystem} button to put on the statusbar.
 * 
 * @author Andrea Antonello - www.hydrologis.com
 *
 *
 *
 * @source $URL$
 */
public class CrsStatusBarButton extends ControlContribution implements MapBoundsListener {

    public final static String ID = "eu.hydrologis.toolbar.toponimicombo"; //$NON-NLS-1$
    private final SwtMapPane mapPane;
    private Button crsButton;
    private MapPaneAdapter mapPaneListener;

    public CrsStatusBarButton( SwtMapPane mapPane ) {
        this(ID, mapPane);
    }

    protected CrsStatusBarButton( String id, SwtMapPane mapPane ) {
        super(id);
        this.mapPane = mapPane;
    }

    @Override
    protected Control createControl( Composite parent ) {
        createListeners();
        mapPane.addMapPaneListener(mapPaneListener);
        mapPane.getMapContent().addMapBoundsListener(this);

        Composite mainComposite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        mainComposite.setLayout(gridLayout);
        StatusLineLayoutData statusLineLayoutData = new StatusLineLayoutData();
        // statusLineLayoutData.widthHint = 500;
        mainComposite.setLayoutData(statusLineLayoutData);

        crsButton = new Button(mainComposite, SWT.PUSH);
        GridData crsButtonGD = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        crsButtonGD.widthHint = 300;
        crsButton.setLayoutData(crsButtonGD);
        CoordinateReferenceSystem crs = getCrs();
        displayCRS(crs);
        crsButton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected( SelectionEvent e ) {
                CRSChooserDialog dialog = new CRSChooserDialog(crsButton.getShell(), getCrs());
                dialog.setBlockOnOpen(true);
                dialog.open();
                CoordinateReferenceSystem newCrs = dialog.getResult();
                mapPane.setCrs(newCrs);
                mapPane.redraw();
                displayCRS(newCrs);
            }
        });

        return mainComposite;
    }

    private CoordinateReferenceSystem getCrs() {
        return mapPane.getMapContent().getCoordinateReferenceSystem();
    }

    private void displayCRS( CoordinateReferenceSystem crs ) {
        if (crs == null) {
            crsButton.setText(Messages.getString("crs_undefined"));
        } else {
            crsButton.setText(crs.getName().toString());
        }
    }

    /**
     * Initialize the mouse and map bounds listeners
     */
    private void createListeners() {
        mapPaneListener = new MapPaneAdapter(){

            @Override
            public void onDisplayAreaChanged( MapPaneEvent ev ) {
                ReferencedEnvelope env = mapPane.getDisplayArea();
                if (env != null) {
                    displayCRS(env.getCoordinateReferenceSystem());
                }
            }

            @Override
            public void onResized( MapPaneEvent ev ) {
                ReferencedEnvelope env = mapPane.getDisplayArea();
                if (env != null) {
                    displayCRS(env.getCoordinateReferenceSystem());
                }
            }

            @Override
            public void onRenderingStarted( MapPaneEvent ev ) {
            }

            @Override
            public void onRenderingStopped( MapPaneEvent ev ) {
            }

            @Override
            public void onRenderingProgress( MapPaneEvent ev ) {
            }

        };
    }

    public void mapBoundsChanged( MapBoundsEvent event ) {
        ReferencedEnvelope env = mapPane.getDisplayArea();
        if (env != null) {
            displayCRS(env.getCoordinateReferenceSystem());
        }
    }

}
