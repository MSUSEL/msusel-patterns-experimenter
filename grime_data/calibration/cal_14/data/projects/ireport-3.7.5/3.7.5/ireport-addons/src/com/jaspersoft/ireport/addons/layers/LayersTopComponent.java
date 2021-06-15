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
package com.jaspersoft.ireport.addons.layers;

import com.jaspersoft.ireport.designer.IReportManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
final class LayersTopComponent extends TopComponent {

    private static LayersTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "LayersTopComponent";

    private LayersTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(LayersTopComponent.class, "CTL_LayersTopComponent"));
        setToolTipText(NbBundle.getMessage(LayersTopComponent.class, "HINT_LayersTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));

        layersListPanel1.addPropertyChangeListener(LayersListPanel.PROPERTY_SELECTED_ITEMS, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                List<Layer> selectedLayers = layersListPanel1.getSelectedLayers();

                for (Layer l : selectedLayers)
                {
                    if (l.isBackgroundLayer())
                    {
                        selectedLayers.remove(l);
                        break;
                    }
                }

                jButtonDeleteLayers.setEnabled(!selectedLayers.isEmpty());
            }
        });

        LayersSupport.getInstance().addLayersChangedListener(new LayersChangedListener() {

            public void layersChanged(LayersChangedEvent event) {
                jButtonNewLayer.setEnabled(event.getJasperDesign() != null);
            }
        });

        jButtonNewLayer.setEnabled(IReportManager.getInstance().getActiveReport() != null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        layersListPanel1 = new com.jaspersoft.ireport.addons.layers.LayersListPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonNewLayer = new javax.swing.JButton();
        jButtonDeleteLayers = new javax.swing.JButton();

        jScrollPane1.setViewportView(layersListPanel1);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButtonNewLayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/addons/layers/new.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButtonNewLayer, org.openide.util.NbBundle.getMessage(LayersTopComponent.class, "LayersTopComponent.jButtonNewLayer.text")); // NOI18N
        jButtonNewLayer.setToolTipText(org.openide.util.NbBundle.getMessage(LayersTopComponent.class, "LayersTopComponent.jButtonNewLayer.toolTipText")); // NOI18N
        jButtonNewLayer.setEnabled(false);
        jButtonNewLayer.setFocusable(false);
        jButtonNewLayer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNewLayer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNewLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewLayerActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonNewLayer);

        jButtonDeleteLayers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/addons/layers/delete.gif"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButtonDeleteLayers, org.openide.util.NbBundle.getMessage(LayersTopComponent.class, "LayersTopComponent.jButtonDeleteLayers.text")); // NOI18N
        jButtonDeleteLayers.setToolTipText(org.openide.util.NbBundle.getMessage(LayersTopComponent.class, "LayersTopComponent.jButtonDeleteLayers.toolTipText")); // NOI18N
        jButtonDeleteLayers.setEnabled(false);
        jButtonDeleteLayers.setFocusable(false);
        jButtonDeleteLayers.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDeleteLayers.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDeleteLayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteLayersActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDeleteLayers);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .add(0, 0, 0)
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNewLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewLayerActionPerformed

        Layer layer = new Layer();



        List<Layer> existingLayers = LayersSupport.getInstance().getLayers();
        List<String> names = new ArrayList<String>();
        int maxid = 0;

        // Find a good name for the layer...
        for (Layer l : existingLayers)
        {
            names.add(l.getName());
            if (maxid <= l.getId()) maxid = l.getId()+1;
        }

        String s = "Layer 1";
        int i=1;
        while (names.contains(s))
        {
            i++;
            s = "Layer " + i;
        }

        layer.setId(maxid);
        layer.setName(s);

        LayersSupport.getInstance().addLayer(layer);
 
    }//GEN-LAST:event_jButtonNewLayerActionPerformed

    private void jButtonDeleteLayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteLayersActionPerformed

        LayersSupport.getInstance().removeLayers(layersListPanel1.getSelectedLayers());

    }//GEN-LAST:event_jButtonDeleteLayersActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDeleteLayers;
    private javax.swing.JButton jButtonNewLayer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private com.jaspersoft.ireport.addons.layers.LayersListPanel layersListPanel1;
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized LayersTopComponent getDefault() {
        if (instance == null) {
            instance = new LayersTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the LayersTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized LayersTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(LayersTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof LayersTopComponent) {
            return (LayersTopComponent) win;
        }
        Logger.getLogger(LayersTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
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
            return LayersTopComponent.getDefault();
        }
    }
}
