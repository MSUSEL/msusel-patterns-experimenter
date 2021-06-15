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
package com.jaspersoft.ireport.designer.data;

import com.jaspersoft.ireport.designer.FieldsContainer;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @version $Id: FieldsContainerTransferHandler.java 0 2010-01-23 10:54:11 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class FieldsContainerTransferHandler  extends TransferHandler  {

    private FieldsContainer fieldsContainer = null;

    public FieldsContainer getFieldsContainer() {
        return fieldsContainer;
    }

    public void setFieldsContainer(FieldsContainer fieldsContainer) {
        this.fieldsContainer = fieldsContainer;
    }

    public FieldsContainerTransferHandler(FieldsContainer fieldsContainer) {
        this.fieldsContainer = fieldsContainer;
    }

    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            try {

                // We assume it is the first flavor...
                JRField field = (JRField)t.getTransferData(t.getTransferDataFlavors()[0]);

                if (fieldsContainer != null)
                {
                    fieldsContainer.addField(field);
                }

                return true;
            } catch (UnsupportedFlavorException ufe) {
            } catch (IOException ioe) {
            }
        }

        return false;
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {

        for (int i = 0; i < flavors.length; i++) {

            if (flavors[i].getRepresentationClass().isInstance(JRField.class));
            {
                return true;
            }
        }
        return false;
    }


}
