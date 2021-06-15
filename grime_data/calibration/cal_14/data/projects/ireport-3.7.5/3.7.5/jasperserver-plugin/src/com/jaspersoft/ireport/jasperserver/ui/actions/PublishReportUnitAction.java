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
package com.jaspersoft.ireport.jasperserver.ui.actions;

import com.jaspersoft.ireport.jasperserver.ui.nodes.FolderNode;
import com.jaspersoft.ireport.jasperserver.ui.nodes.ResourceNode;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.JComponent;
import org.openide.util.ContextAwareAction;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author gtoffoli
 */
public class PublishReportUnitAction extends CallableSystemAction implements ContextAwareAction, LookupListener {

    public Action createContextAwareInstance(Lookup arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    private final Lookup lkp;
    private final Lookup.Result <FolderNode> result;
    
    public PublishReportUnitAction() {
        this (Utilities.actionsGlobalContext());
    }
    
    
    private PublishReportUnitAction (Lookup lkp) {
        this.lkp = lkp;
        result = lkp.lookup(new Lookup.Template(FolderNode.class));
        result.addLookupListener(this);
        resultChanged (null);
    }

    public void resultChanged(LookupEvent arg0) {
        this.setEnabled( result.allInstances().size() > 0);
    }

    @Override
    public void performAction() {
        
        Collection<? extends FolderNode>nodes = result.allInstances();
        if (nodes.size() > 0)
        {
            ResourceNode selectedNode = (ResourceNode)nodes.iterator().next();
            SystemAction.get(AddResourceAction.class).addResource(selectedNode, ResourceDescriptor.TYPE_REPORTUNIT);
        }
    }

    public String getName() {
        return NbBundle.getMessage(PublishReportUnitAction.class, "CTL_PublishReportUnitAction");
    }

  
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected String iconResource() {
        return "com/jaspersoft/ireport/jasperserver/res/publish_report.png";
    }

    @Override
    protected void initialize() {
        super.initialize();
        putValue(Action.SHORT_DESCRIPTION, NbBundle.getMessage(PublishReportUnitAction.class, "CTL_PublishReportUnitAction"));
    
    }

 
}
