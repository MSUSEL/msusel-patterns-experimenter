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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.text.DefaultEditorKit;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 * Top component which displays something.
 */
final public class GenericInspectorTopComponent extends TopComponent implements ExplorerManager.Provider {

    private static GenericInspectorTopComponent instance;
    /** path to the icon used by the component and its open action */
    static final public String ICON_PATH = "com/jaspersoft/ireport/designer/resources/inspector.png";

    private static final String PREFERRED_ID = "GenericInspectorTopComponent";

    /** Dynamic Lookup content */
    private final InstanceContent ic;
    /** Lookup instance */
    private final Lookup lookup;

    private BeanTreeView view;
    private final ExplorerManager manager = new ExplorerManager();
    private TopComponent currentVisualView = null;
    private boolean updatingSelection = false;
    private AbstractNode noReportNode = null;

    //private Lookup.Result<ChartThemeNode> rootNode = null

    private GenericInspectorTopComponent() {
        initComponents();
        setName(I18n.getString("CTL_GenericInspectorTopComponent"));
        setToolTipText(I18n.getString("HINT_GenericInspectorTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        this.ic = new InstanceContent();
        this.lookup = new AbstractLookup(ic);

        noReportNode = new AbstractNode(Children.LEAF);
        noReportNode.setDisplayName("No Chart Theme available");

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

        manager.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName()))
                {
                    if (isUpdatingSelection()) return;
                    if (getCurrentVisualView() != null && getCurrentVisualView() instanceof ExplorerManager.Provider)
                    {
                        try {
                            ((ExplorerManager.Provider)getCurrentVisualView()).getExplorerManager().setSelectedNodes(getExplorerManager().getSelectedNodes());
                        } catch (PropertyVetoException ex) {
                            //Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        });

        getExplorerManager().setRootContext(noReportNode);
    }

    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public TopComponent getCurrentVisualView() {
        return currentVisualView;
    }

    /**
     * This method is called when a JRCTX view is shown...
     * or hidden (in that case the view is null...)
     * @param view
     */
    public void setCurrentVisualView(TopComponent view) {
        
        currentVisualView = view;
        if (currentVisualView == null)
        {

            getExplorerManager().setRootContext(noReportNode);
        }
        else
        {
            setUpdatingSelection(true);
            try {
                if (currentVisualView instanceof ExplorerManager.Provider)
                {
                    getExplorerManager().setRootContext(((ExplorerManager.Provider)currentVisualView).getExplorerManager().getRootContext());
                    getExplorerManager().setSelectedNodes(((ExplorerManager.Provider)currentVisualView).getExplorerManager().getSelectedNodes());
                }
            } catch (PropertyVetoException ex) {
                //Exceptions.printStackTrace(ex);
            }
            setUpdatingSelection(false);
        }
        return;
    }

    public void closingVisualView(TopComponent view) {

        if (view == currentVisualView)
        {
            setCurrentVisualView(null);
        }

    }

    public synchronized boolean isUpdatingSelection() {
        return updatingSelection;
    }

    public synchronized void setUpdatingSelection(boolean updatingSelection) {
        this.updatingSelection = updatingSelection;
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
            .add(0, 295, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 368, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized GenericInspectorTopComponent getDefault() {
        if (instance == null) {
            instance = new GenericInspectorTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the JRCTXInspectorTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized GenericInspectorTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(GenericInspectorTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof GenericInspectorTopComponent) {
            return (GenericInspectorTopComponent) win;
        }
        Logger.getLogger(GenericInspectorTopComponent.class.getName()).warning(
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

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return GenericInspectorTopComponent.getDefault();
        }
    }

}
