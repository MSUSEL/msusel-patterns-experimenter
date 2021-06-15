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
package com.jaspersoft.ireport.designer.resourcebundle;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.text.DefaultEditorKit;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.explorer.view.TreeView;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.text.DataEditorSupport;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
final class ResourceBundleTopComponent extends TopComponent implements ExplorerManager.Provider, PropertyChangeListener {

    private static ResourceBundleTopComponent instance;

    transient protected TreeView view;

    /** Dynamic Lookup content */
    private final InstanceContent ic;
    /** Lookup instance */
    private final Lookup lookup;
    /** validity flag */
    transient private boolean valid = true;
    private final ExplorerManager manager = new ExplorerManager();

    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";

    private static final String PREFERRED_ID = "ResourceBundleTopComponent";

    //private final Lookup.Result <DataObject> result;

    private ResourceBundleTopComponent() {
        initComponents();

        this.ic = new InstanceContent();
        this.lookup = new AbstractLookup(ic);

        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, true));

        associateLookup( new ProxyLookup(lookup, ExplorerUtils.createLookup(manager, map)) );

        setLayout(new BorderLayout());
        view = new BeanTreeView();
        //view.setRootVisible(false);
        add(view, BorderLayout.CENTER);
        //view.setRootVisible(false);

        setName(NbBundle.getMessage(ResourceBundleTopComponent.class, "CTL_ResourceBundleTopComponent"));
        setToolTipText(NbBundle.getMessage(ResourceBundleTopComponent.class, "HINT_ResourceBundleTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));
//        result = Utilities.actionsGlobalContext().lookup(new Lookup.Template(DataObject.class));
//        result.addLookupListener(this);
//        result.allItems();
        //updateTree(false);

        //WindowManager.getDefault().getRegistry().addPropertyChangeListener(this);

    }

    private DataObject findDataObject(TopComponent tc) {
        if (tc ==null) return null;
        System.out.println(tc.getLookup().lookupAll(Object.class));
        System.out.flush();
        DataObject dObj = tc.getLookup().lookup(DataObject.class);
        if (dObj != null) return dObj;
        DataEditorSupport des = tc.getLookup().lookup(DataEditorSupport.class);
        if (des != null) return des.getDataObject();
        return null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ResourceBundleTopComponent getDefault() {
        if (instance == null) {
            instance = new ResourceBundleTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ResourceBundleTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ResourceBundleTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ResourceBundleTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ResourceBundleTopComponent) {
            return (ResourceBundleTopComponent) win;
        }
        Logger.getLogger(ResourceBundleTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }


    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(  Registry.PROP_ACTIVATED ))
        {
            System.out.println("TopComponent activated..." + " " + evt.getPropertyName() + " " + evt.getNewValue());
            System.out.flush();
            updateTree(false);
        }
        else if (evt.getPropertyName().equals(  Registry.PROP_OPENED ))
        {
            System.out.println("TopComponent opened..." + " " + evt.getPropertyName() + " " + evt.getNewValue());
            System.out.flush();
            updateTree(false);
        }
        else if (evt.getPropertyName().equals(  Registry.PROP_TC_OPENED))
        {
            System.out.println("TopComponent TC opened..." + " " + evt.getPropertyName() + " " + evt.getNewValue());
            System.out.flush();
            updateTree(false);
        }
        else if (evt.getPropertyName().equals(  Registry.PROP_TC_CLOSED))
        {
            lastSelectedTopComponent = null;
            getExplorerManager().setRootContext(new AbstractNode(Children.LEAF));

           
            System.out.println("TopComponent TC closed..." + " " + evt.getPropertyName() + " " + evt.getNewValue());
            System.out.println("Still open: " + WindowManager.getDefault().getRegistry().getOpened() + "");
            System.out.flush();
        }
        
    }

    TopComponent lastSelectedTopComponent = null;
    public void updateTree(boolean closing)
    {
        // Don't do anything if we have not selected an editor...
        TopComponent selectedTc = WindowManager.getDefault().getRegistry().getActivated();
        if (selectedTc != null &&
            !WindowManager.getDefault().isEditorTopComponent(selectedTc))
        {
            System.out.println("Activated not an editor...");
            lastSelectedTopComponent = null;
            return;
        }

        if (lastSelectedTopComponent == null ||
            WindowManager.getDefault().getRegistry().getActivated() != lastSelectedTopComponent && !closing)
        {
            lastSelectedTopComponent = WindowManager.getDefault().getRegistry().getActivated();
            if (lastSelectedTopComponent != null)
            {
                DataObject dobj = findDataObject(lastSelectedTopComponent);
                System.out.println("Found data object: " + dobj);
                if (dobj != null)
                {
                    DataObject nDO = null;

                    if (dobj.getPrimaryFile() != null &&
                        dobj.getPrimaryFile().getParent() != null)
                    {
                        try {
                            nDO = DataObject.find(dobj.getPrimaryFile().getParent());
                            if (nDO != null)
                            {
                                getExplorerManager().setRootContext(nDO.getNodeDelegate());
                                return;
                            }
                        } catch (DataObjectNotFoundException ex) {
                            //Exceptions.printStackTrace(ex);
                            ex.printStackTrace();
                        }
                    }
                }
            }
            //getExplorerManager().setRootContext(new AbstractNode(Children.LEAF));
        }
        
        if (lastSelectedTopComponent == null ||
            !lastSelectedTopComponent.isValid() ||
            !lastSelectedTopComponent.isVisible())
        {
            getExplorerManager().setRootContext(new AbstractNode(Children.LEAF));
        }
    }


    public ExplorerManager getExplorerManager() {
        return manager;
    }

}
