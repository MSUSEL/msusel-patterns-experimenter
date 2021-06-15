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

public final class CategoryDatasetVisualPanel3 extends JPanel {

    private JRDesignDataset lastDs = null;

    CategoryDatasetWizardPanel3 wizardPanel = null;
    /** Creates new form CategoryDatasetVisualPanel2 */
    public CategoryDatasetVisualPanel3(CategoryDatasetWizardPanel3 panel) {
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

        jRTextExpressionCategory.getExpressionEditorPane().getDocument().addDocumentListener(dl);
        jRTextExpressionValue.getExpressionEditorPane().getDocument().addDocumentListener(dl);

    }


    private void fireChangeEvent()
    {
        getWizardPanel().fireChangeEvent();
    }


    public void validateForm()
    {
        String category = jRTextExpressionCategory.getText();
        if (category.trim().length() == 0)
        {
           throw new IllegalArgumentException(NbBundle.getMessage(PieVisualPanel1.class, "CategoryDatasetVisualPanel2.invalidCategoryExpression"));
        }
        String value = jRTextExpressionValue.getText();
        if (value.trim().length() == 0)
        {
           throw new IllegalArgumentException(NbBundle.getMessage(PieVisualPanel1.class, "CategoryDatasetVisualPanel2.invalidValueExpression"));
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(PieVisualPanel1.class, "CategoryDatasetVisualPanel3.name");
    }

    public void readSettings(Object settings) {

        JRDesignDataset ds = (JRDesignDataset) ((WizardDescriptor)settings).getProperty("dataset");

        if (lastDs != ds)
        {
            jRTextExpressionCategory.setExpressionContext(new ExpressionContext(ds));
            jRTextExpressionCategory.setText("");
            jRTextExpressionValue.setExpressionContext(new ExpressionContext(ds));
            jRTextExpressionValue.setText("");

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
                    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/charts/datasets/wizards/stack.png"))); // NOI18N
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
        ((WizardDescriptor)settings).putProperty("categoryExpression", jRTextExpressionCategory.getText());
        ((WizardDescriptor)settings).putProperty("valueExpression", jRTextExpressionValue.getText());
    }

    /**
     * @return the wizardPanel
     */
    public CategoryDatasetWizardPanel3 getWizardPanel() {
        return wizardPanel;
    }

    /**
     * @param wizardPanel the wizardPanel to set
     */
    public void setWizardPanel(CategoryDatasetWizardPanel3 wizardPanel) {
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
        jRTextExpressionCategory = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jRTextExpressionValue = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel3.class, "CategoryDatasetVisualPanel3.jLabel2.text")); // NOI18N

        jRTextExpressionCategory.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionCategory.setMinimumSize(null);
        jRTextExpressionCategory.setPreferredSize(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel3.class, "CategoryDatasetVisualPanel3.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel3.class, "CategoryDatasetVisualPanel3.jLabel4.text")); // NOI18N
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(CategoryDatasetVisualPanel3.class, "CategoryDatasetVisualPanel3.jLabel3.text")); // NOI18N

        jRTextExpressionValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionValue.setMinimumSize(null);
        jRTextExpressionValue.setPreferredSize(null);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRTextExpressionCategory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .add(jRTextExpressionValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRTextExpressionCategory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRTextExpressionValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionCategory;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionValue;
    // End of variables declaration//GEN-END:variables
}

