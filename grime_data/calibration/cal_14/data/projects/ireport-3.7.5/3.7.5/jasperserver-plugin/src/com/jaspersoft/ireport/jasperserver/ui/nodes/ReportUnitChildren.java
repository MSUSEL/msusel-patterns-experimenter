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

import com.jaspersoft.ireport.jasperserver.RepositoryFile;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.RepositoryReportUnit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class ReportUnitChildren extends Index.KeysChildren implements PropertyChangeListener {

    private RepositoryReportUnit reportUnit = null;
    private Lookup doLkp = null;
    private ReportUnitInputControlsNode controlsNode = null;
    private ReportUnitResourcesNode resourcesNode = null;
    private boolean calculating = false;
            
       
    public ReportUnitChildren(RepositoryReportUnit reportUnit, Lookup doLkp) {
        super(new ArrayList());
        this.reportUnit = reportUnit;
        this.doLkp = doLkp;
        controlsNode = new ReportUnitInputControlsNode(getReportUnit(), doLkp);
        resourcesNode = new ReportUnitResourcesNode(getReportUnit(), doLkp);
    }

    /*
    @Override
    protected List<Node> initCollection() {
        return recalculateKeys();
    }
    */
    
    
    protected Node[] createNodes(Object key) {
        
        if (key instanceof RepositoryFile)
        {
            return new Node[]{new FileNode((RepositoryFile)key, doLkp)};
        }
        else if (key instanceof Node)
        {
            return new Node[]{(Node)key};
        }
        return new Node[]{};
    }
    
    
    
    @Override
    protected void addNotify() {
        super.addNotify();
        recalculateKeys();
    }
    
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys() {

        recalculateKeys(false);
    }
    
    public void recalculateKeys(final boolean reload) {
        if (isCalculating()) return;
        setCalculating(true);
        final List l = (List)lock();
        l.clear();
        
        // We dived here connection, main jrxml, input controls and resources 
        
        Runnable run = new Runnable() {

            public void run() {
                
                SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                       ((ReportUnitNode)getNode()).setLoading(true);
                    }
                });
                
                List children = getReportUnit().getChildren(reload);
                
                boolean mainReportFound = false;
                
                for (int i=0; i<children.size(); ++i)
                {
                    
                    RepositoryFolder item = (RepositoryFolder)children.get(i);
                    
                    if (item.isDataSource()) continue;
                    if (item.getDescriptor().isMainReport())
                    {
                        if (!mainReportFound) // This check is a fix for a WS bug that sends this descriptor twice
                        {
                            l.add(item);
                        }
                        mainReportFound = true;
                    }
                }
                l.add(controlsNode);
                l.add(resourcesNode);
                    
                SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                       update();
                       ((InputControlsChildren)controlsNode.getChildren()).recalculateKeys();
                       ((ResourcesChildren)resourcesNode.getChildren()).recalculateKeys();
                       ((ReportUnitNode)getNode()).setLoading(false);
                       setCalculating(false);
                    }
                });
            }
        };
        
        Thread t = new Thread(run);
        t.start();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(ReportUnitChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        
    }

    public RepositoryReportUnit getReportUnit() {
        return reportUnit;
    }

    public void setReportUnit(RepositoryReportUnit reportUnit) {
        this.reportUnit = reportUnit;
    }

    /**
     * @return the calculating
     */
    public boolean isCalculating() {
        return calculating;
    }

    /**
     * @param calculating the calculating to set
     */
    public void setCalculating(boolean calculating) {
        this.calculating = calculating;
    }
}
