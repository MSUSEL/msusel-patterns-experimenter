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
package com.jaspersoft.ireport.designer.utils;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataFolder;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.actions.Presenter;
import org.openide.util.lookup.Lookups;

public class SubMenuAction extends AbstractAction implements HelpCtx.Provider, Presenter.Popup {


    private static Map<String,SubMenuAction> nodeActionsMap = new HashMap<String,SubMenuAction>();

    private JMenu menu = null;
    private String layerPath = null;

    public static SubMenuAction getAction(String layerPath)
    {
        SubMenuAction action = null;
        if (!nodeActionsMap.containsKey(layerPath))
        {
            action = new SubMenuAction(layerPath);
            nodeActionsMap.put(layerPath, action);
        }
        else
        {
            action = nodeActionsMap.get(layerPath);
        }
    
        // update status...
        return action;
    }

    private SubMenuAction(String layerPath) {
        this.layerPath = layerPath;
    }

    public String getName() {
        return I18n.getString(getLayerPath());
    }

//    @Override
//    protected void initialize() {
//        super.initialize();
//        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
//        putValue("noIconInMenu", Boolean.TRUE); // NOI18N
//    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    
    public JMenuItem getPopupPresenter() {

        
            menu = new JMenu(getName());

            FileObject nodesFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject(getLayerPath());
            
            if (nodesFileObject == null) return null;
            DataFolder nodesDataFolder = DataFolder.findFolder(nodesFileObject);

            if (nodesDataFolder == null) return null;

            Lookup lookup = Lookups.forPath(getLayerPath()); // NOI18N
            Collection<? extends Object> nodeActions = lookup.lookupAll(Object.class);

            Lookup context = Lookup.getDefault();

            Iterator<? extends Object> it = nodeActions.iterator();
            while (it.hasNext ()) {
                
                Object obj =it.next();
                if (obj instanceof Action)
                {
                    JMenuItem mi = new JMenuItem();
                    org.openide.awt.Actions.connect(mi, (Action)obj, true);
                    Icon icon = (Icon) ((Action)obj).getValue( Action.SMALL_ICON );
                    if (icon != null) mi.setIcon(icon);
                    menu.add(mi);
                }
                else if (obj instanceof JSeparator)
                {
                    menu.add((JSeparator)obj);
                }
            }
            /*
            Enumeration<DataObject> enObj = nodesDataFolder.children();
            while (enObj.hasMoreElements())
            {
                DataObject dataObject = enObj.nextElement();
                NodeAction nodeAction = dataObject.getLookup().lookup(NodeAction.class);

                if (nodeAction != null)
                {
                    menu.add(nodeAction.getMenuPresenter());
                }
            }
            */
        

        return menu;
    }
//
//    @Override
//    protected boolean asynchronous() {
//        return false;
//    }

    /**
     * @return the layerPath
     */
    public String getLayerPath() {
        return layerPath;
    }

    /**
     * @param layerPath the layerPath to set
     */
    public void setLayerPath(String layerPath) {
        this.layerPath = layerPath;
    }

//    @Override
//    protected void performAction(Node[] arg0) {
//    }
//
//    @Override
//    protected boolean enable(Node[] arg0) {
//        return true;
//    }

    public void actionPerformed(ActionEvent arg0) {

    }


}
