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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.components.table.wizard;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.crosstab.wizard.DatasetListCellRenderer;
import com.jaspersoft.ireport.designer.undo.AddDatasetUndoableEdit;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.wizards.DatasetWizardIterator;
import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;

public final class TableVisualPanel1 extends JPanel {

    private TableWizardPanel1 wizardPanel = null;
    /** Creates new form TableVisualPanel1 */
    public TableVisualPanel1(TableWizardPanel1 wizardPanel) {
        initComponents();
        this.wizardPanel = wizardPanel;
        jComboBoxDataset.setRenderer( new DatasetListCellRenderer() );
        updateDatasets();

        if (jComboBoxDataset.getItemCount() > 0)
        {
            jRadioButton1.setSelected(true);
        }
        else
        {
            jRadioButton2.setSelected(true);
        }
    }

    @Override
    public String getName() {
        return "New Table";
    }

    public void updateDatasets() {

        DefaultComboBoxModel model = (DefaultComboBoxModel)jComboBoxDataset.getModel();
        model.removeAllElements();

        JasperDesign design = IReportManager.getInstance().getActiveReport();
        if (design == null) return;

        List datasets = design.getDatasetsList();
        //model.addElement( design.getMainDataset());
        for (int i=0; i<datasets.size(); ++i)
        {
            model.addElement( datasets.get(i));
        }

        if (jComboBoxDataset.getItemCount() > 0)
        {
            jComboBoxDataset.setSelectedIndex(0);
        }
        updateOptions();
        jComboBoxDataset.updateUI();
    }


    public void validateForm()
    {
        return;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jComboBoxDataset = new javax.swing.JComboBox();
        jLabelNoDatasets = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        buttonGroup1.add(jRadioButton1);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton1, org.openide.util.NbBundle.getMessage(TableVisualPanel1.class, "TableVisualPanel1.jRadioButton1.text")); // NOI18N
        jRadioButton1.setEnabled(false);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton2, org.openide.util.NbBundle.getMessage(TableVisualPanel1.class, "TableVisualPanel1.jRadioButton2.text")); // NOI18N
        jRadioButton2.setActionCommand(org.openide.util.NbBundle.getMessage(TableVisualPanel1.class, "TableVisualPanel1.jRadioButton2.actionCommand")); // NOI18N
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jComboBoxDataset.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxDataset.setEnabled(false);
        jComboBoxDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxDatasetActionPerformed(evt);
            }
        });

        jLabelNoDatasets.setForeground(new java.awt.Color(153, 0, 51));
        org.openide.awt.Mnemonics.setLocalizedText(jLabelNoDatasets, org.openide.util.NbBundle.getMessage(TableVisualPanel1.class, "TableVisualPanel1.jLabelNoDatasets.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(TableVisualPanel1.class, "TableVisualPanel1.jLabel2.text")); // NOI18N

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(4), Integer.valueOf(1), null, Integer.valueOf(1)));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(TableVisualPanel1.class, "TableVisualPanel1.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(TableVisualPanel1.class, "TableVisualPanel1.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(27, 27, 27)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jRadioButton2))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jRadioButton1))
            .add(layout.createSequentialGroup()
                .add(27, 27, 27)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jComboBoxDataset, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 208, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1)
                        .addContainerGap())
                    .add(jLabelNoDatasets, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(25, 25, 25)
                .add(jRadioButton1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jComboBoxDataset, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelNoDatasets)
                .add(30, 30, 30)
                .add(jRadioButton2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(jSpinner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel3)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        getWizardPanel().updateWizardPanels();
        getWizardPanel().fireChangeEvent();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        getWizardPanel().updateWizardPanels();
        getWizardPanel().fireChangeEvent();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {
            DatasetWizardIterator iterator = new DatasetWizardIterator();
            WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
            iterator.initialize(wizardDescriptor, getJasperDesign() );
            // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
            // {1} will be replaced by WizardDescriptor.Iterator.name()
            wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
            wizardDescriptor.setTitle("New Dataset");
            Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
            dialog.setVisible(true);
            dialog.toFront();
            boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
            if (!cancelled) {

                JRDesignDataset newDataset = new JRDesignDataset(false);
                newDataset.setName( (String) wizardDescriptor.getProperty("dataset_name"));

                List<JRDesignField> selectedFields = (List<JRDesignField>) wizardDescriptor.getProperty("selectedFields");
                List<JRDesignField> groupFields = (List<JRDesignField>) wizardDescriptor.getProperty("groupFields");
                String query = (String) wizardDescriptor.getProperty("query");
                String queryLanguage = (String) wizardDescriptor.getProperty("queryLanguage");

                if (selectedFields == null) selectedFields = new ArrayList<JRDesignField>();
                if (groupFields == null) groupFields = new ArrayList<JRDesignField>();

                // Adding fields
                for (JRDesignField f : selectedFields)
                {
                    newDataset.addField(f);
                }

                // Query...
                if (query != null)
                {
                    JRDesignQuery designQuery = new JRDesignQuery();
                    designQuery.setText(query);
                    if (queryLanguage != null)
                    {
                        designQuery.setLanguage(queryLanguage);
                    }

                    newDataset.setQuery(designQuery);
                }

                // Adjusting groups
                for (int i=0; i<groupFields.size(); ++i)
                {
                      JRDesignGroup g =new JRDesignGroup();
                      g.setName(groupFields.get(i).getName());
                      g.setExpression(Misc.createExpression(groupFields.get(i).getValueClassName(), "$F{" + groupFields.get(i).getName() + "}"));
                      newDataset.addGroup(g);
                }

                getJasperDesign().addDataset(newDataset);
                AddDatasetUndoableEdit edit = new AddDatasetUndoableEdit(newDataset, getJasperDesign());
                IReportManager.getInstance().addUndoableEdit(edit);

                updateDatasets();
                jComboBoxDataset.setSelectedItem(newDataset);
                jRadioButton1.setSelected(true);
                jRadioButton2.setSelected(false);

            }

        } catch (JRException ex) {
            Exceptions.printStackTrace(ex);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBoxDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxDatasetActionPerformed

    }//GEN-LAST:event_jComboBoxDatasetActionPerformed


    public void updateOptions()
    {
        if (jComboBoxDataset.getItemCount() > 0)
        {
            jRadioButton1.setEnabled(true);
            jComboBoxDataset.setEnabled(true);
            jLabelNoDatasets.setText(" ");
        }
        else
        {
            jComboBoxDataset.setEnabled(false);
            jRadioButton1.setEnabled(false);
            jLabelNoDatasets.setText(org.openide.util.NbBundle.getMessage(TableVisualPanel1.class, "TableVisualPanel1.jLabelNoDatasets.text"));
        }
    }

    public JasperDesign getJasperDesign()
    {
        return (JasperDesign) this.getWizardPanel().getWizard().getProperty("jasperdesign");
    }

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    public void readSettings(Object settings) {
    }

    public void storeSettings(Object settings) {
        getWizardPanel().getWizard().putProperty("table_type", jRadioButton1.isSelected() ? 0 : 1);
        getWizardPanel().getWizard().putProperty("dataset", jComboBoxDataset.getSelectedItem());
        if (jComboBoxDataset.getSelectedItem() != null)
        {
            List<JRDesignField> list = new ArrayList<JRDesignField>();
            List fieldsList = ((JRDesignDataset)jComboBoxDataset.getSelectedItem()).getFieldsList();
            for (int i=0; i<fieldsList.size(); ++i)
            {
                list.add((JRDesignField)fieldsList.get(i));
            }
            getWizardPanel().getWizard().putProperty("discoveredFields", list );
            getWizardPanel().getWizard().putProperty("discoveredFieldsNeedRefresh","true");
        }
        getWizardPanel().getWizard().putProperty("columns", jSpinner1.getValue());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxDataset;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelNoDatasets;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the wizardPanel
     */
    public TableWizardPanel1 getWizardPanel() {
        return wizardPanel;
    }

    public boolean isFinishPanel()
    {
        return jRadioButton2.isSelected();
    }
}

