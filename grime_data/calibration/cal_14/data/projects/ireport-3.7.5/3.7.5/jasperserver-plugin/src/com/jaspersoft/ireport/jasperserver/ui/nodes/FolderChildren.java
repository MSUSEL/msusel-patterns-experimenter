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
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Mutex;

/**
 *
 * @author gtoffoli
 */
public class FolderChildren extends Index.KeysChildren implements PropertyChangeListener {

    private RepositoryFolder folder = null;
    private Lookup doLkp = null;
    private AbstractNode parentNode = null;
    private boolean calculating = false;

    public FolderChildren(RepositoryFolder folder, Lookup doLkp) {
        super(new ArrayList());
        this.folder = folder;
        this.doLkp = doLkp;
    }

    @Override
    protected void reorder(int[] permutations) {
        // reordering does nothing...
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
        if (key instanceof RepositoryReportUnit)
        {
            return new Node[]{new ReportUnitNode((RepositoryReportUnit)key, doLkp)};
        }
        else if (key instanceof RepositoryFolder)
        {
            return new Node[]{new FolderNode((RepositoryFolder)key, doLkp)};
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
    
    @SuppressWarnings("unchecked")
    public void recalculateKeys(final boolean refresh) {
        if (isCalculating()) return;
        setCalculating(true);

        final List l = (List)lock();
        l.clear();
        List params = null;
        
        Runnable run = new Runnable() {

            public void run() {
                
                SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                       ((FolderNode)getNode()).setLoading(true);
                    }
                });
                
                List children = folder.getChildren(true);
                if (children != null)
                {
                    l.addAll( children );
                    SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                           update();
                           ((FolderNode)getNode()).setLoading(false);
                           setCalculating(false);
                        }
                    });
                }
                else
                {
                    folder.setLoaded(false);
                    ((FolderNode)getNode()).setLoading(false);
                    setCalculating(false);
                }
            }
        };
        
        Thread t = new Thread(run);
        t.start();
    }
    
    @SuppressWarnings("unchecked")
    public void reorder() { 
            Mutex.Action action = new Mutex.Action(){ 
                public Object run(){ 
                    Index.Support.showIndexedCustomizer(FolderChildren.this.getIndex()); 
                    return null; 
                } 
            }; 
            MUTEX.writeAccess(action); 
        }

    public void propertyChange(PropertyChangeEvent evt) {
        
    }

    public RepositoryFolder getFolder() {
        return folder;
    }

    public void setFolder(RepositoryFolder folder) {
        this.folder = folder;
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
