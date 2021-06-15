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
package com.jaspersoft.ireport.designer.options.export;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import java.util.prefs.Preferences;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 *
 * @author gtoffoli
 */
public class XlsExportParametersPanel extends AbstractExportParametersPanel {

    /** Creates new form XlsExportParametersPanel */
    public XlsExportParametersPanel() {
        initComponents();
        SpinnerNumberModel snm = new SpinnerNumberModel(0,0, Integer.MAX_VALUE,1);
        jSpinnerMaximumRowsPerSheet.setModel(snm);

        snm.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                notifyChange();
            }
        });

        applyI18n();
    }

    public void applyI18n()
    {
        jLabelTitle.setText(I18n.getString("XlsExportParametersPanel.jLabelTitle.text")); // NOI18N
        jCheckBoxOnePagePerSheet.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxOnePagePerSheet.text")); // NOI18N
        jCheckBoxRemoveEmptySpaceBetweenRows.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxRemoveEmptySpaceBetweenRows.text")); // NOI18N
        jCheckBoxRemoveEmptySpaceBetweenColumns.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxRemoveEmptySpaceBetweenColumns.text")); // NOI18N
        jCheckBoxWhitePageBackground.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxWhitePageBackground.text")); // NOI18N
        jCheckBoxAutoDetectCellType.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxAutoDetectCellType.text")); // NOI18N
        jCheckBoxFontSizeFixEnabled.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxFontSizeFixEnabled.text")); // NOI18N
        jCheckBoxImageBorderFixEnabled.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxImageBorderFixEnabled.text")); // NOI18N
        jLabelMaximumRowsPerSheet.setText(I18n.getString("XlsExportParametersPanel.jLabelMaximumRowsPerSheet.text")); // NOI18N
        jCheckBoxIgnoreGraphics.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxIgnoreGraphics.text")); // NOI18N
        jCheckBoxCollapseRowSpan.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxCollapseRowSpan.text")); // NOI18N
        jCheckBoxIgnoreCellBorder.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxIgnoreCellBorder.text")); // NOI18N
        jTabbedPane1.setTitleAt(0, I18n.getString("XlsExportParametersPanel.jPanel1.TabConstraints.tabTitle")); // NOI18N
        jCheckBoxUseSheetNames.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxUseSheetNames.text")); // NOI18N
        jLabelList.setText(I18n.getString("XlsExportParametersPanel.jLabelList.text")); // NOI18N
        jTabbedPane1.setTitleAt(1, I18n.getString("XlsExportParametersPanel.jPanel2.TabConstraints.tabTitle")); // NOI18N
        jCheckBoxCreateCustomPalette.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxCreateCustomPalette.text")); // NOI18N
        jLabelPassword.setText(I18n.getString("XlsExportParametersPanel.jLabelPassword.text")); // NOI18N
        jTabbedPane1.setTitleAt(2, I18n.getString("XlsExportParametersPanel.jPanel3.TabConstraints.tabTitle")); // NOI18N
        jCheckBoxIgnoreCellBackground.setText(I18n.getString("XlsExportParametersPanel.jCheckBoxIgnoreCellBackground.text")); // NOI18N
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jCheckBoxOnePagePerSheet = new javax.swing.JCheckBox();
        jCheckBoxRemoveEmptySpaceBetweenRows = new javax.swing.JCheckBox();
        jCheckBoxRemoveEmptySpaceBetweenColumns = new javax.swing.JCheckBox();
        jCheckBoxWhitePageBackground = new javax.swing.JCheckBox();
        jCheckBoxAutoDetectCellType = new javax.swing.JCheckBox();
        jCheckBoxFontSizeFixEnabled = new javax.swing.JCheckBox();
        jCheckBoxImageBorderFixEnabled = new javax.swing.JCheckBox();
        jLabelMaximumRowsPerSheet = new javax.swing.JLabel();
        jSpinnerMaximumRowsPerSheet = new javax.swing.JSpinner();
        jCheckBoxIgnoreGraphics = new javax.swing.JCheckBox();
        jCheckBoxCollapseRowSpan = new javax.swing.JCheckBox();
        jCheckBoxIgnoreCellBorder = new javax.swing.JCheckBox();
        jCheckBoxIgnoreCellBackground = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxUseSheetNames = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabelList = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBoxCreateCustomPalette = new javax.swing.JCheckBox();
        jLabelPassword = new javax.swing.JLabel();
        jTextFieldPassword = new javax.swing.JTextField();

        jLabelTitle.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabelTitle.setText("CSV Export parameters");

        jCheckBoxOnePagePerSheet.setText("One Page per Sheet");
        jCheckBoxOnePagePerSheet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxOnePagePerSheetActionPerformed(evt);
            }
        });

        jCheckBoxRemoveEmptySpaceBetweenRows.setText("Remove Empty Space Between Rows");
        jCheckBoxRemoveEmptySpaceBetweenRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRemoveEmptySpaceBetweenRowsActionPerformed(evt);
            }
        });

        jCheckBoxRemoveEmptySpaceBetweenColumns.setText("Remove Empty Space Between Columns");
        jCheckBoxRemoveEmptySpaceBetweenColumns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxRemoveEmptySpaceBetweenColumnsActionPerformed(evt);
            }
        });

        jCheckBoxWhitePageBackground.setText("White Page Background");
        jCheckBoxWhitePageBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxWhitePageBackgroundActionPerformed(evt);
            }
        });

        jCheckBoxAutoDetectCellType.setText("Auto Detect Cell Type");
        jCheckBoxAutoDetectCellType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAutoDetectCellTypeActionPerformed(evt);
            }
        });

        jCheckBoxFontSizeFixEnabled.setText("Font Size Fix Enabled");
        jCheckBoxFontSizeFixEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFontSizeFixEnabledActionPerformed(evt);
            }
        });

        jCheckBoxImageBorderFixEnabled.setText("Image Border Fix Enabled");
        jCheckBoxImageBorderFixEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxImageBorderFixEnabledActionPerformed(evt);
            }
        });

        jLabelMaximumRowsPerSheet.setText("Maximum Rows Per Sheet (0 means no maximum)");

        jCheckBoxIgnoreGraphics.setText("Ignore Graphics");
        jCheckBoxIgnoreGraphics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIgnoreGraphicsActionPerformed(evt);
            }
        });

        jCheckBoxCollapseRowSpan.setText("Collapse Row Span");
        jCheckBoxCollapseRowSpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCollapseRowSpanActionPerformed(evt);
            }
        });

        jCheckBoxIgnoreCellBorder.setText("Ignore Cell Border");
        jCheckBoxIgnoreCellBorder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIgnoreCellBorderActionPerformed(evt);
            }
        });

        jCheckBoxIgnoreCellBackground.setText("Ignore Cell Background");
        jCheckBoxIgnoreCellBackground.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxIgnoreCellBackgroundActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxIgnoreCellBackground)
                    .add(jCheckBoxOnePagePerSheet)
                    .add(jCheckBoxRemoveEmptySpaceBetweenRows)
                    .add(jCheckBoxRemoveEmptySpaceBetweenColumns)
                    .add(jCheckBoxWhitePageBackground)
                    .add(jCheckBoxAutoDetectCellType)
                    .add(jCheckBoxFontSizeFixEnabled)
                    .add(jCheckBoxImageBorderFixEnabled)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jLabelMaximumRowsPerSheet)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSpinnerMaximumRowsPerSheet, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jCheckBoxIgnoreGraphics)
                    .add(jCheckBoxCollapseRowSpan)
                    .add(jCheckBoxIgnoreCellBorder))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxOnePagePerSheet)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxRemoveEmptySpaceBetweenRows)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxRemoveEmptySpaceBetweenColumns)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxWhitePageBackground)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxAutoDetectCellType)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxFontSizeFixEnabled)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxImageBorderFixEnabled)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelMaximumRowsPerSheet)
                    .add(jSpinnerMaximumRowsPerSheet, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxIgnoreGraphics)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxCollapseRowSpan)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxIgnoreCellBorder)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBoxIgnoreCellBackground)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Common", jPanel1);

        jCheckBoxUseSheetNames.setText("Use Sheet Names");
        jCheckBoxUseSheetNames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxUseSheetNamesActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabelList.setText("Sheet names. Each row corresponds to one sheet name.");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxUseSheetNames)
                    .add(jLabelList)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxUseSheetNames)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabelList)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .add(17, 17, 17))
        );

        jTabbedPane1.addTab("Sheet Names", jPanel2);

        jCheckBoxCreateCustomPalette.setText("Create Custom Palette");
        jCheckBoxCreateCustomPalette.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCreateCustomPaletteActionPerformed(evt);
            }
        });

        jLabelPassword.setText("Password");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxCreateCustomPalette)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabelPassword)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(212, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBoxCreateCustomPalette)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabelPassword)
                    .add(jTextFieldPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(245, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("JExcelAPI options", jPanel3);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addContainerGap(276, Short.MAX_VALUE))
            .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabelTitle)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxOnePagePerSheetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxOnePagePerSheetActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxOnePagePerSheetActionPerformed

    private void jCheckBoxRemoveEmptySpaceBetweenRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRemoveEmptySpaceBetweenRowsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxRemoveEmptySpaceBetweenRowsActionPerformed

    private void jCheckBoxRemoveEmptySpaceBetweenColumnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxRemoveEmptySpaceBetweenColumnsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxRemoveEmptySpaceBetweenColumnsActionPerformed

    private void jCheckBoxWhitePageBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxWhitePageBackgroundActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxWhitePageBackgroundActionPerformed

    private void jCheckBoxAutoDetectCellTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAutoDetectCellTypeActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxAutoDetectCellTypeActionPerformed

    private void jCheckBoxFontSizeFixEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFontSizeFixEnabledActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxFontSizeFixEnabledActionPerformed

    private void jCheckBoxImageBorderFixEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxImageBorderFixEnabledActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxImageBorderFixEnabledActionPerformed

    private void jCheckBoxIgnoreGraphicsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIgnoreGraphicsActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxIgnoreGraphicsActionPerformed

    private void jCheckBoxCollapseRowSpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCollapseRowSpanActionPerformed
       notifyChange();
    }//GEN-LAST:event_jCheckBoxCollapseRowSpanActionPerformed

    private void jCheckBoxIgnoreCellBorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIgnoreCellBorderActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxIgnoreCellBorderActionPerformed

    private void jCheckBoxUseSheetNamesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxUseSheetNamesActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxUseSheetNamesActionPerformed

    private void jCheckBoxCreateCustomPaletteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCreateCustomPaletteActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxCreateCustomPaletteActionPerformed

    private void jCheckBoxIgnoreCellBackgroundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxIgnoreCellBackgroundActionPerformed
        notifyChange();
    }//GEN-LAST:event_jCheckBoxIgnoreCellBackgroundActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxAutoDetectCellType;
    private javax.swing.JCheckBox jCheckBoxCollapseRowSpan;
    private javax.swing.JCheckBox jCheckBoxCreateCustomPalette;
    private javax.swing.JCheckBox jCheckBoxFontSizeFixEnabled;
    private javax.swing.JCheckBox jCheckBoxIgnoreCellBackground;
    private javax.swing.JCheckBox jCheckBoxIgnoreCellBorder;
    private javax.swing.JCheckBox jCheckBoxIgnoreGraphics;
    private javax.swing.JCheckBox jCheckBoxImageBorderFixEnabled;
    private javax.swing.JCheckBox jCheckBoxOnePagePerSheet;
    private javax.swing.JCheckBox jCheckBoxRemoveEmptySpaceBetweenColumns;
    private javax.swing.JCheckBox jCheckBoxRemoveEmptySpaceBetweenRows;
    private javax.swing.JCheckBox jCheckBoxUseSheetNames;
    private javax.swing.JCheckBox jCheckBoxWhitePageBackground;
    private javax.swing.JLabel jLabelList;
    private javax.swing.JLabel jLabelMaximumRowsPerSheet;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinnerMaximumRowsPerSheet;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextFieldPassword;
    // End of variables declaration//GEN-END:variables


    public void load() {
        setInit(true);
        Preferences pref = IReportManager.getPreferences();

        jCheckBoxCreateCustomPalette.setSelected( pref.getBoolean(JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE, JRProperties.getBooleanProperty(JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE)));
        jTextFieldPassword.setText(  pref.get(JExcelApiExporterParameter.PROPERTY_PASSWORD, JRProperties.getProperty(JExcelApiExporterParameter.PROPERTY_PASSWORD)));

        jCheckBoxCollapseRowSpan.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxAutoDetectCellType.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_DETECT_CELL_TYPE, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxFontSizeFixEnabled.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxIgnoreCellBorder.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BORDER, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxIgnoreCellBackground.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND)));

        jCheckBoxIgnoreGraphics.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS)));
        jCheckBoxImageBorderFixEnabled.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IMAGE_BORDER_FIX_ENABLED, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxOnePagePerSheet.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxRemoveEmptySpaceBetweenColumns.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxRemoveEmptySpaceBetweenRows.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));
        jCheckBoxWhitePageBackground.setSelected( pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN)));

        SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerMaximumRowsPerSheet.getModel();
        model.setValue( pref.getInt(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET, JRProperties.getIntegerProperty(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET)));

        jCheckBoxUseSheetNames.setSelected( pref.getBoolean(JRProperties.PROPERTY_PREFIX + "export.xls.useSheetNames", false));

        jTextArea1.setText(pref.get(JRProperties.PROPERTY_PREFIX + "export.xls.sheetNames", ""));
        setInit(false);
    }

    public void store() {

        Preferences pref = IReportManager.getPreferences();

        pref.putBoolean(JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE,  jCheckBoxCreateCustomPalette.isSelected() );
        pref.put(JExcelApiExporterParameter.PROPERTY_PASSWORD, jTextFieldPassword.getText());
        
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN,  jCheckBoxCollapseRowSpan.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_DETECT_CELL_TYPE,  jCheckBoxAutoDetectCellType.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED,  jCheckBoxFontSizeFixEnabled.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BORDER,  jCheckBoxIgnoreCellBorder.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND,  jCheckBoxIgnoreCellBackground.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS,  jCheckBoxIgnoreGraphics.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_IMAGE_BORDER_FIX_ENABLED,  jCheckBoxImageBorderFixEnabled.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET,  jCheckBoxOnePagePerSheet.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,  jCheckBoxRemoveEmptySpaceBetweenColumns.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,  jCheckBoxRemoveEmptySpaceBetweenRows.isSelected() );
        pref.putBoolean(JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND,  jCheckBoxWhitePageBackground.isSelected() );

        SpinnerNumberModel model = (SpinnerNumberModel)jSpinnerMaximumRowsPerSheet.getModel();
        pref.putInt(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET, model.getNumber().intValue());

        pref.putBoolean(JRProperties.PROPERTY_PREFIX + "export.xls.useSheetNames",  jCheckBoxUseSheetNames.isSelected() );
        pref.put(JRProperties.PROPERTY_PREFIX + "export.xls.sheetNames", jTextArea1.getText().trim());

    }

    public boolean valid() {
        return true;
    }

    @Override
    public String getDisplayName() {
        return I18n.getString("XlsExportParametersPanel.title");
    }
}
