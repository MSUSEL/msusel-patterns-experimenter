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
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.swing.process;

import java.awt.Dialog;
import org.geotools.swing.wizard.*;
import java.awt.HeadlessException;

/**
 * Wizard to walk users through running a process.
 * <p>
 * This class is public and can be used in your own application:
 * <pre><code>
 * TBD
 * </pre></code>
 * <p>
 * If you wish to customize this wizard (as an example to allow users to
 * draw a geometry directly rather than use WKT) pelase use this class
 * as a starting point or code example With this in mind the two pages
 * used here are intended for reuse.
 * <p>
 * The *ProcessSelectionPage* is interesting in that it defines the next
 * page that is to be displayed. It does this based on the selected process
 * description and is a good example of an dynamic wizard.
 * 
 * @author Jody Garnett (LISAsoft)
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class JProcessWizard extends JWizard {
    private static final long serialVersionUID = -5885825548881784615L;
    private ProcessSelectionPage selectionPage;
    
    /** Create a JProcessWizard for use */
    public JProcessWizard(String title) throws HeadlessException {
        this(null,title != null ? title : "Process Wizard" );
    }

    public JProcessWizard( Dialog owner, String title){
        super( owner, title );
        selectionPage = new ProcessSelectionPage();
        
        selectionPage.setPageIdentifier("select");
        selectionPage.setBackPageIdentifier(null); // start!
        selectionPage.setNextPageIdentifier("param");
        registerWizardPanel( selectionPage );
        
        setCurrentPanel( "select" );
    }

    public static void main( String args[] ){
        JProcessWizard wizard = new JProcessWizard("Test Input Parameter UI");
        int result = wizard.showModalDialog();
        
        System.out.println("finished " + result );
    }
}
