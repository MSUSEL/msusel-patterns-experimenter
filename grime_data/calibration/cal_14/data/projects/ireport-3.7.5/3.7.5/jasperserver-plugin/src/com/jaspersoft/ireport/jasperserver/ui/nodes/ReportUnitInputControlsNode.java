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
package com.jaspersoft.ireport.jasperserver.ui.nodes;

import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.actions.AddExistingInputControlAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.AddInputControlAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RunReportUnitAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RunReportUnitCookieImpl;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ReportUnitInputControlsNode extends IRIndexedNode implements ResourceNode {

    private Lookup doLkp;
    private RepositoryReportUnit reportUnit;
            
    
    public ReportUnitInputControlsNode(RepositoryReportUnit reportUnit, Lookup doLkp) {
        this(new InputControlsChildren(reportUnit, doLkp), reportUnit, doLkp);
    }
    
    public ReportUnitInputControlsNode(InputControlsChildren pc, RepositoryReportUnit reportUnit, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(new RunReportUnitCookieImpl(), reportUnit, reportUnit.getServer())));
        this.reportUnit = reportUnit;
        setDisplayName("Input controls");
        setIconBaseWithExtension("/com/jaspersoft/ireport/jasperserver/res/folder.png");
        getLookup().lookup(RunReportUnitCookieImpl.class).setNode(this);
    }

    public RepositoryReportUnit getReportUnit() {
        return reportUnit;
    }

    public void setReportUnit(RepositoryReportUnit reportUnit) {
        this.reportUnit = reportUnit;
    }
    
    @Override
    public Action[] getActions(boolean b) {
        
        List<Action> actions = new ArrayList<Action>();

        actions.add( SystemAction.get(AddExistingInputControlAction.class));
        actions.add( SystemAction.get(AddInputControlAction.class));

        actions.add(null);
        actions.add( SystemAction.get(RunReportUnitAction.class));

//        if (getParentNode() != null)
//        {
//            Action[] parentActions = getParentNode().getActions(b);
//            for (int i=0; i<parentActions.length; ++i)
//            {
//                actions.add(  parentActions[i] );
//            }
//        }
        
        return actions.toArray(new Action[actions.size()]);
    }

    public ResourceDescriptor getResourceDescriptor() {
        return getReportUnit().getDescriptor();
    }

    public RepositoryFolder getRepositoryObject() {
        return getReportUnit();
    }

    public void refreshChildrens(boolean reload) {
        if (getParentNode() != null &&
            getParentNode() instanceof ReportUnitNode)
        {
            ((ReportUnitNode)getParentNode()).refreshChildrens(reload);
        }
    }
    
    public void updateDisplayName() {
        fireDisplayNameChange(null,null);
    }
    
    
}
