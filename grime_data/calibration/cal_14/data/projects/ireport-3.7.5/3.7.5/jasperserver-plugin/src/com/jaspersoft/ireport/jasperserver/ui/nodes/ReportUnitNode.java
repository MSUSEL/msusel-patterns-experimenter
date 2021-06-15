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

import com.jaspersoft.ireport.designer.dnd.ReportObjectPaletteTransferable;
import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import com.jaspersoft.ireport.jasperserver.ui.actions.AddResourceAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.DeleteAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.PropertiesAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RefreshAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RunReportUnitAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RunReportUnitCookieImpl;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class ReportUnitNode extends IRIndexedNode implements ResourceNode {

    private RepositoryReportUnit reportUnit = null;
    private boolean loading = false;
    
    protected static final ImageIcon loadingIcon = new ImageIcon(FolderNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/waiting.png"));
    protected static final ImageIcon reportUnitIcon = new ImageIcon(ReportUnitNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/reportunit.png"));
    
    public boolean hasCustomizer() {
        return false;
    }
    
    public ReportUnitNode(RepositoryReportUnit reportUnit, Lookup doLkp) {
        this(new ReportUnitChildren(reportUnit, doLkp), reportUnit, doLkp);
    }
    
    public ReportUnitNode(ReportUnitChildren pc, RepositoryReportUnit reportUnit, Lookup doLkp) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(new RunReportUnitCookieImpl(), reportUnit, reportUnit.getServer())));
        this.reportUnit = reportUnit;
        getLookup().lookup(RunReportUnitCookieImpl.class).setNode(this);
    }

    @Override
    public String getDisplayName() {
        return getReportUnit().getDescriptor().getLabel() + ( (isLoading()) ? " (Loading....)" : "");
    }

    @Override
    public Image getIcon(int arg0) {
    
        if (isLoading())
        {
            return loadingIcon.getImage();
        }
        return reportUnitIcon.getImage();
    }
    
    @Override
    public Image getOpenedIcon(int arg0) {
    
        if (isLoading())
        {
            return loadingIcon.getImage();
        }
        return reportUnitIcon.getImage();
    
    }
    
    

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        this.fireIconChange();
    }

    public RepositoryReportUnit getReportUnit() {
        return reportUnit;
    }

    public void setReportUnit(RepositoryReportUnit reportUnit) {
        this.reportUnit = reportUnit;
    }
    
    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{
            SystemAction.get( RunReportUnitAction.class),
            SystemAction.get( AddResourceAction.class),
            null,
            SystemAction.get( CopyAction.class),
            SystemAction.get( CutAction.class),
            SystemAction.get( DeleteAction.class),
            null,
            SystemAction.get( RefreshAction.class),
            SystemAction.get( PropertiesAction.class)
        };
    }

    public ResourceDescriptor getResourceDescriptor() {
        return getReportUnit().getDescriptor();
    }

    public RepositoryFolder getRepositoryObject() {
        return getReportUnit();
    }

    public void refreshChildrens(boolean reload) {
        ReportUnitChildren children = (ReportUnitChildren)getChildren();
        children.recalculateKeys(reload);
    }
    
    public void updateDisplayName() {
        fireDisplayNameChange(null,null);
    }
    
    public static RepositoryReportUnit getParentReportUnit(Node node)
    {
        if (node == null) return null;
        
        if ( (node instanceof ResourceNode) &&
            ((ResourceNode)node).getRepositoryObject() instanceof RepositoryReportUnit)
        {
            return (RepositoryReportUnit)((ResourceNode)node).getRepositoryObject();
        }
        
        return getParentReportUnit(node.getParentNode());
    }

 
     @Override
    public Transferable drag() throws IOException {
        ExTransferable tras = ExTransferable.create(clipboardCut());
        
        tras.put(new ReportObjectPaletteTransferable( 
                    "com.jaspersoft.ireport.jasperserver.ui.actions.CreateDrillDownAction",
                    getReportUnit()));
        return tras;
    }


    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canCut() {
        return true;
    }

}
