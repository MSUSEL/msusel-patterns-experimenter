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

import com.jaspersoft.ireport.designer.dnd.DnDUtilities;
import com.jaspersoft.ireport.designer.outline.nodes.IRIndexedNode;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.ireport.jasperserver.ui.actions.AddResourceAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.DeleteAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.DeleteServerAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.ModifyServerAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.NewServerAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.PropertiesAction;
import com.jaspersoft.ireport.jasperserver.ui.actions.RefreshAction;
import com.jaspersoft.ireport.jasperserver.ui.nodes.dnd.FolderPasteType;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.PasteAction;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.nodes.NodeTransfer;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author gtoffoli
 */
public class FolderNode extends IRIndexedNode implements ResourceNode {

    private boolean root = false;
    private RepositoryFolder folder = null;
    private boolean loading = false;
    
    protected static final ImageIcon loadingIcon = new ImageIcon(FolderNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/waiting.png"));
    protected static final ImageIcon serverIcon =  new ImageIcon(FolderNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/server.png"));
    protected static final ImageIcon folderIcon =  new ImageIcon(FolderNode.class.getResource("/com/jaspersoft/ireport/jasperserver/res/folder.png"));
    
    public boolean hasCustomizer() {
        return false;
    }
    
    public FolderNode(RepositoryFolder folder, Lookup doLkp) {
        this(new FolderChildren(folder, doLkp), folder, doLkp, false);
    }
    
    public FolderNode(RepositoryFolder folder, Lookup doLkp, boolean root) {
        this(new FolderChildren(folder, doLkp), folder, doLkp, root);
    }

    public FolderNode(FolderChildren pc, RepositoryFolder folder, Lookup doLkp, boolean root) {
        super(pc, pc.getIndex(), new ProxyLookup(doLkp, Lookups.fixed(folder, folder.getServer())));
        this.root = root;
        this.folder = folder;
    }

    

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public RepositoryFolder getFolder() {
        return folder;
    }

    public void setFolder(RepositoryFolder folder) {
        this.folder = folder;
    }

    @Override
    public String getDisplayName() {
        return getFolder().getDescriptor().getLabel() + ( (isLoading()) ? " (Loading....)" : "");
    }
    
    @Override
    public Image getIcon(int arg0) {
    
        if (isLoading())
        {
            return loadingIcon.getImage();
        }
        
        if (isRoot())
        {
            return serverIcon.getImage();
        }
        else
        {
            return folderIcon.getImage();
        }
    }
    
    @Override
    public Image getOpenedIcon(int arg0) {
    
        if (isLoading())
        {
            return loadingIcon.getImage();
        }
        else if (isRoot())
        {
            return serverIcon.getImage();
        }
        return folderIcon.getImage();
    
    }
    
    

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        this.fireIconChange();
    }

    @Override
    public Action[] getActions(boolean arg0) {


        List<Action> list = new ArrayList();
        list.add(SystemAction.get( AddResourceAction.class));
        list.add(null);

        if (isRoot())
        {

            list.add(SystemAction.get( NewServerAction.class));
            list.add(SystemAction.get( ModifyServerAction.class));
            list.add(SystemAction.get( DeleteServerAction.class));
            list.add(SystemAction.get( AddResourceAction.class));
            list.add(SystemAction.get( PasteAction.class ));
        }
        else
        {
            list.add(SystemAction.get( CopyAction.class ));
            list.add(SystemAction.get( CutAction.class ));
            list.add(SystemAction.get( PasteAction.class ));
            list.add(SystemAction.get( DeleteAction.class));
        }
        list.add(null);
        list.add(SystemAction.get( RefreshAction.class));
        list.add(SystemAction.get( PropertiesAction.class));

        return list.toArray(new Action[list.size()]);
    }

    
    public ResourceDescriptor getResourceDescriptor() {
        return getFolder().getDescriptor();
    }

    public RepositoryFolder getRepositoryObject() {
        return getFolder();
    }

    public void refreshChildrens(boolean reload) {

        Object children = getChildren();
        if (children instanceof FolderChildren)
        {
            ((FolderChildren)children).recalculateKeys(reload);
        }
        else if (children instanceof ServerChildren)
        {
            ((ServerChildren)children).recalculateKeys();
        }
    }
    
    public void updateDisplayName() {
        fireDisplayNameChange(null,null);
    }


    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {

        Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
    
        return FolderPasteType.createFolderPasteType( DnDUtilities.getTransferAction(t), this, dropNode);

    }

    @Override
    public boolean canCopy() {
        return !isRoot();
    }

    @Override
    public boolean canCut() {
        return !isRoot();
    }



    @SuppressWarnings("unchecked")
    @Override
    protected void createPasteTypes(Transferable t, List s) {
        super.createPasteTypes(t, s);
        PasteType paste = getDropType(t, DnDConstants.ACTION_MOVE, -1);
        if (null != paste) {
            s.add(paste);
        }
    }
    
}
