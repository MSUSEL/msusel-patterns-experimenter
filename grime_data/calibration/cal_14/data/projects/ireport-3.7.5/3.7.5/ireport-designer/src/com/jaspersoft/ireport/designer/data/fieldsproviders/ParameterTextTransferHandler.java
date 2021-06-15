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
package com.jaspersoft.ireport.designer.data.fieldsproviders;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import net.sf.jasperreports.engine.design.JRDesignParameter;

/**
 *
 * @version $Id: ParameterTextTransferHandler.java 0 2010-09-08 18:59:49 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class ParameterTextTransferHandler  extends TransferHandler {

    int action = TransferHandler.MOVE;

    @Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
        this.action = action;
        super.exportAsDrag(comp, e, action);
    }




    @Override
  protected Transferable createTransferable(JComponent c) {

    String text = "";
    if (c instanceof JList)
    {
        JList jList = (JList)c;
        if (jList.getSelectedValue() instanceof JRDesignParameter)
        {
            JRDesignParameter p = (JRDesignParameter) jList.getSelectedValue();
            text =  ((action == MOVE) ? "$P{" : "$P!{")+ p.getName() + "}";
        }
    }
    
    return new StringSelection(text);
  }

  public int getSourceActions(JComponent c) {
    return COPY_OR_MOVE;
  }

  public boolean importData(JComponent c, Transferable t) {
    return false;
  }

  protected void exportDone(JComponent c, Transferable data, int action) {
  }

  public boolean canImport(JComponent c, DataFlavor[] flavors) {
    return false;
  }
}

