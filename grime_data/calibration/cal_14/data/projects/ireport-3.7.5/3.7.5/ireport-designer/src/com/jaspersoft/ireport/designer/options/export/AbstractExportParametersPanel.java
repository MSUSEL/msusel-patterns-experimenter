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
package com.jaspersoft.ireport.designer.options.export;

import javax.swing.JPanel;
import com.jaspersoft.ireport.designer.options.OptionsPanel;
import com.jaspersoft.ireport.designer.options.IReportOptionsPanelController;

/**
 *
 * @author gtoffoli
 */
public abstract class AbstractExportParametersPanel extends JPanel implements OptionsPanel {

    private IReportOptionsPanelController controller = null;

    private boolean init = false;

    public boolean setInit(boolean b)
    {
        boolean old = init;
        init =b;
        return old;
    }

    public boolean isInit()
    {
        return init;
    }

    public AbstractExportParametersPanel()
    {
    }

    /**
     * Notify a change in the UI.
     */
    public void notifyChange()
    {
        if (this.getController() != null && !isInit())
        {
            getController().changed();
        }
    }

    /**
     * @return the controller
     */
    public IReportOptionsPanelController getController() {
        return controller;
    }
    
    /**
     * The contorller is always set by iReport before to use the panel...
     * @return the controller
     */
    public void setController(IReportOptionsPanelController ctrl) {
        this.controller = ctrl;
    }




    /**
     * return the name that should appear in the exporters list
     * @return
     */
    abstract public String getDisplayName();
}
