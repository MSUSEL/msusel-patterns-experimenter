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
/**
 * 
 */
package org.geotools.swt.control;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swt.utils.Messages;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CRSChooserDialog extends Dialog {
    private final CRSChooser chooser = new CRSChooser();
    private final CoordinateReferenceSystem initialValue;
    private CoordinateReferenceSystem result;

    public CRSChooserDialog( Shell parentShell, CoordinateReferenceSystem initialValue ) {
        super(parentShell);
        this.initialValue = initialValue;
    }

    @Override
    protected Control createDialogArea( Composite parent ) {
        getShell().setText(Messages.getString("CRSChooserDialog_title"));
        chooser.setController(new Controller(){

            public void handleClose() {
                close();
            }

            public void handleOk() {
                result = chooser.getCRS();
            }

        });

        Control control = chooser.createControl(parent, initialValue);
        chooser.setFocus();
        return control;
    }

    @Override
    public boolean close() {
        result = chooser.getCRS();
        return super.close();
    }

    public CoordinateReferenceSystem getResult() {
        return result;
    }

    public static void main( String[] args ) {
        CRSChooserDialog dialog = new CRSChooserDialog(new Shell(Display.getDefault()), DefaultGeographicCRS.WGS84);
        dialog.setBlockOnOpen(true);
        dialog.open();

        CoordinateReferenceSystem crs = dialog.getResult();
        System.out.println(crs);

    }
}