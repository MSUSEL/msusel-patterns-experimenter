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
package com.jaspersoft.ireport.designer.menu;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportDesignerPanel;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Iterator;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;

public final class OpenQueryDialogAction extends CallableSystemAction implements LookupListener {

    private final Lookup lkp;
    private final Lookup.Result <? extends ReportDesignerPanel> result;

    
    public void performAction() {
       
        Window pWin = Misc.getMainWindow();
        ReportQueryDialog rqd = null;
        if (pWin instanceof Dialog) rqd = new ReportQueryDialog((Dialog)pWin, true);
        else if (pWin instanceof Frame) rqd = new ReportQueryDialog((Frame)pWin, true);
        else rqd = new ReportQueryDialog((Dialog)null, true);

           
        rqd.setDataset( IReportManager.getInstance().getActiveReport().getMainDesignDataset() );
        rqd.setVisible(true);
    }

    public void resultChanged(LookupEvent e) {
        
        updateStatus();
    }
    
    public void updateStatus()
    {
        Iterator<? extends ReportDesignerPanel> i = result.allInstances().iterator();
        if (i.hasNext())
        {
            setEnabled(true);
        }
        else
        {
            setEnabled(false);
        }
    }
    
    public OpenQueryDialogAction(){
            this (Utilities.actionsGlobalContext());
    }
    
    private OpenQueryDialogAction(Lookup lkp) {
        
        this.lkp = lkp;
        result = lkp.lookupResult(ReportDesignerPanel.class);
        result.addLookupListener(this);
        result.allItems();
        updateStatus();
    }
            
    public String getName() {
        return NbBundle.getMessage(OpenQueryDialogAction.class, "CTL_OpenQueryDialogAction");
    }

    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/designer/resources/query-16.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    
}