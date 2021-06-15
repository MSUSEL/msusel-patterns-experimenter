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
package com.jaspersoft.jrx;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.options.export.AbstractExportParametersPanel;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import java.util.prefs.Preferences;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 *
 * @author gtoffoli
 */
public class TextExportParametersPanel extends AbstractExportParametersPanel {

    /** Creates new form OpenOfficeExportParametersPanel */
    public TextExportParametersPanel() {
        initComponents();

        ChangeListener snmcl = new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    notifyChange();
                }
        };

        SpinnerNumberModel snm = new SpinnerNumberModel(0,0, Integer.MAX_VALUE,1);
        jSpinnerPageWidth.setModel(snm);
        snm.addChangeListener(snmcl);

        snm = new SpinnerNumberModel(0,0, Integer.MAX_VALUE,1);
        jSpinnerPageHeight.setModel(snm);
        snm.addChangeListener(snmcl);

         javax.swing.event.DocumentListener textfieldListener =  new javax.swing.event.DocumentListener()
        {
            public void changedUpdate(javax.swing.event.DocumentEvent evt)
            {
                notifyChange();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt)
            {
                notifyChange();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt)
            {
                notifyChange();
            }
        };

         //.getDocument().addDocumentListener(textfieldListener);
         jTextFieldBIDIPrefix.getDocument().addDocumentListener(textfieldListener);

        applyI18n();
    }

    public void applyI18n()
    {
        jLabelTitle.setText(I18n.getString("IReportTextExportParametersPanel.jLabelTitle.text")); // NOI18N
        jLabelPageWidth.setText(I18n.getString("TextExportParametersPanel.jLabelPageWidth.text")); // NOI18N
        jLabelPageHeight.setText(I18n.getString("TextExportParametersPanel.jLabelPageHeight.text")); // NOI18N
        jLabelBIDIPrefix.setText(I18n.getString("IReportTextExportParametersPanel.jLabelBIDIPrefix.text")); // NOI18N
        jLabelDisplayWidthProviderFactory.setText(I18n.getString("IReportTextExportParametersPanel.jLabelDisplayWidthProviderFactory.text")); // NOI18N
        jCheckBoxAddFormFeed.setText(I18n.getString("IReportTextExportParametersPanel.jCheckBoxAddFormFeed.text")); // NOI18N
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelTitle = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelPageWidth = new javax.swing.JLabel();
        jSpinnerPageWidth = new javax.swing.JSpinner();
        jLabelPageHeight = new javax.swing.JLabel();
        jSpinnerPageHeight = new javax.swing.JSpinner();
        jLabelBIDIPrefix = new javax.swing.JLabel();
        jTextFieldBIDIPrefix = new javax.swing.JTextField();
        jLabelDefault3 = new javax.swing.JLabel();
        jLabelDefault4 = new javax.swing.JLabel();
        jCheckBoxAddFormFeed = new javax.swing.JCheckBox();
        jLabelDisplayWidthProviderFactory = new javax.swing.JLabel();
        jTextFieldDisplayWidthProviderFactory = new javax.swing.JTextField();

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelTitle.setText("Text (iReport) Export parameters");

        jLabelPageWidth.setText("Page Width");

        jLabelPageHeight.setText("Page Height");

        jLabelBIDIPrefix.setText("BIDI prefix");

        jLabelDefault3.setText("(0 to use default)");

        jLabelDefault4.setText("(0 to use default)");

        jCheckBoxAddFormFeed.setText("Add form feed");
        jCheckBoxAddFormFeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAddFormFeedActionPerformed(evt);
            }
        });

        jLabelDisplayWidthProviderFactory.setText("Display Width Provider Factory");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addContainerGap(215, Short.MAX_VALUE))
            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jLabelBIDIPrefix)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldBIDIPrefix))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBoxAddFormFeed)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelPageWidth)
                            .add(jLabelPageHeight))
                        .add(28, 28, 28)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jSpinnerPageWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jSpinnerPageHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelDefault3)
                            .add(jLabelDefault4))))
                .addContainerGap(191, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jTextFieldDisplayWidthProviderFactory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelDisplayWidthProviderFactory)
                .addContainerGap(262, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPageWidth)
                    .add(jSpinnerPageWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDefault3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPageHeight)
                    .add(jSpinnerPageHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabelDefault4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBoxAddFormFeed)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelBIDIPrefix)
                    .add(jTextFieldBIDIPrefix, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jLabelDisplayWidthProviderFactory)
                .add(0, 0, 0)
                .add(jTextFieldDisplayWidthProviderFactory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(182, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxAddFormFeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAddFormFeedActionPerformed
        notifyChange();
}//GEN-LAST:event_jCheckBoxAddFormFeedActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxAddFormFeed;
    private javax.swing.JLabel jLabelBIDIPrefix;
    private javax.swing.JLabel jLabelDefault3;
    private javax.swing.JLabel jLabelDefault4;
    private javax.swing.JLabel jLabelDisplayWidthProviderFactory;
    private javax.swing.JLabel jLabelPageHeight;
    private javax.swing.JLabel jLabelPageWidth;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinnerPageHeight;
    private javax.swing.JSpinner jSpinnerPageWidth;
    private javax.swing.JTextField jTextFieldBIDIPrefix;
    private javax.swing.JTextField jTextFieldDisplayWidthProviderFactory;
    // End of variables declaration//GEN-END:variables

    public void load() {
        Preferences pref = IReportManager.getPreferences();

        SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerPageHeight.getModel();
        model.setValue( pref.getInt( "irtext.pageHeight", 0) );

        model = (SpinnerNumberModel)jSpinnerPageWidth.getModel();
        model.setValue( pref.getInt( "irtext.pageWidth", 0) );

        jCheckBoxAddFormFeed.setSelected( pref.getBoolean("irtext.addFormFeed", true) );
        jTextFieldBIDIPrefix.setText( pref.get("irtext.bidi", ""));
        jTextFieldDisplayWidthProviderFactory.setText( pref.get("irtext.displaywidthProviderFactory", ""));

    }

    public void store() {

        Preferences pref = IReportManager.getPreferences();

        SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerPageHeight.getModel();
        pref.putInt("irtext.pageHeight", model.getNumber().intValue());

        model = (SpinnerNumberModel)jSpinnerPageWidth.getModel();
        pref.putInt("irtext.pageWidth", model.getNumber().intValue());

        pref.putBoolean("irtext.addFormFeed", jCheckBoxAddFormFeed.isSelected());
        pref.put("irtext.bidi", jTextFieldBIDIPrefix.getText());
        pref.put("irtext.displaywidthProviderFactory", jTextFieldDisplayWidthProviderFactory.getText());

    }

    public boolean valid() {
        return true;
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("format.irtxt");
    }
}
