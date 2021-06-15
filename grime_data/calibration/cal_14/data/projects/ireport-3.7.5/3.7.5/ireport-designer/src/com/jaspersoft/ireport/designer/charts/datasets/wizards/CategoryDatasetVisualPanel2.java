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
package com.jaspersoft.ireport.designer.charts.datasets.wizards;

import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle;

public final class CategoryDatasetVisualPanel2 extends JPanel {

    private JRDesignDataset lastDs = null;

    CategoryDatasetWizardPanel2 wizardPanel = null;
    /** Creates new form CategoryDatasetVisualPanel2 */
    public CategoryDatasetVisualPanel2(CategoryDatasetWizardPanel2 panel) {
        initComponents();
        this.wizardPanel = panel;

        DocumentListener dl = new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                fireChangeEvent();
            }

            public void removeUpdate(DocumentEvent e) {
                fireChangeEvent();
            }

            public void changedUpdate(DocumentEvent e) {
                fireChangeEvent();
            }
        };

        jRTextExpressionSeries.getExpressionEditorPane().getDocument().addDocumentListener(dl);

    }


    private void fireChangeEvent()
    {
        getWizardPanel().fireChangeEvent();
    }


    public void validateForm()
    {
        String series = jRTextExpressionSeries.getText();
        if (series.trim().length() == 0)
        {
           throw new IllegalArgumentException(NbBundle.getMessage(PieVisualPanel1.class, "CategoryDatasetVisualPanel2.invalidSeriesExpression"));
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(PieVisualPanel1.class, "CategoryDatasetVisualPanel2.name");
    }

    public void readSettings(Object settings) {

        JRDesignDataset ds = (JRDesignDataset) ((WizardDescriptor)settings).getProperty("dataset");

        if (lastDs != ds)
        {
            jRTextExpressionSeries.setExpressionContext(new ExpressionContext(ds));
            jRTextExpressionSeries.setText(org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel2.class, "CategoryDatasetVisualPanel2.jRTextExpressionSeries.text")); // NOI18N


        }


        Byte b = (Byte) ((WizardDescriptor)settings).getProperty("chartType");
        if (b != null)
        {
            switch (b.byteValue())
            {
                case JRChart.CHART_TYPE_BAR:
                case JRChart.CHART_TYPE_BAR3D:
                    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/datasets/wizards/bar.png"))); // NOI18N
                    break;
                case JRChart.CHART_TYPE_STACKEDBAR:
                case JRChart.CHART_TYPE_STACKEDBAR3D:
                    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/datasets/wizards/stacked.png"))); // NOI18N
                    break;
                case JRChart.CHART_TYPE_LINE:
                    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/datasets/wizards/line.png"))); // NOI18N
                    break;
                case JRChart.CHART_TYPE_AREA:
                    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/datasets/wizards/area.png"))); // NOI18N
                    break;
                case JRChart.CHART_TYPE_STACKEDAREA:
                    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/datasets/wizards/stackarea.png"))); // NOI18N
                    break;
            }
        }
    }

    public void storeSettings(Object settings) {
        ((WizardDescriptor)settings).putProperty("seriesExpression", jRTextExpressionSeries.getText());
    }

    /**
     * @return the wizardPanel
     */
    public CategoryDatasetWizardPanel2 getWizardPanel() {
        return wizardPanel;
    }

    /**
     * @param wizardPanel the wizardPanel to set
     */
    public void setWizardPanel(CategoryDatasetWizardPanel2 wizardPanel) {
        this.wizardPanel = wizardPanel;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jRTextExpressionSeries = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel2.class, "CategoryDatasetVisualPanel2.jLabel2.text")); // NOI18N

        jRTextExpressionSeries.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionSeries.setPreferredSize(null);
        jRTextExpressionSeries.setText(org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel2.class, "CategoryDatasetVisualPanel2.jRTextExpressionSeries.text")); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/datasets/wizards/bar.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel2.class, "CategoryDatasetVisualPanel2.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel2.class, "CategoryDatasetVisualPanel2.jLabel4.text")); // NOI18N
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                    .add(jRTextExpressionSeries, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRTextExpressionSeries, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionSeries;
    // End of variables declaration//GEN-END:variables
}

