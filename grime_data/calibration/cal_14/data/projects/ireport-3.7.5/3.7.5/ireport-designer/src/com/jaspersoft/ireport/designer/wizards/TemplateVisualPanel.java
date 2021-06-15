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
package com.jaspersoft.ireport.designer.wizards;

import com.jaspersoft.ireport.locale.I18n;
import javax.swing.JPanel;

public final class TemplateVisualPanel extends JPanel {

    private TemplateWizardPanel panel = null;
        
    /** Creates new form TemplateVisualPanel */
    public TemplateVisualPanel(TemplateWizardPanel panel) {
        this.panel = panel;
        initComponents();
        //jList1.setModel(new DefaultListModel());
        //jList1.setCellRenderer(new TemplateVisualPanel.FieldListCellRenderer());
        //updateTemplates();
    }

    @Override
    public String getName() {
        return I18n.getString("TemplateVisualPanel.Name.Layout");
    }

    /*
    private void updateTemplates()
    {
        loadTemplates( getReportType() );
    }


    private void loadTemplates(String type)
    {
        
        jLabelPreview.setIcon(null);
        ((DefaultListModel)jList1.getModel()).clear();
        FileObject templatesFileObject = Repository.getDefault().getDefaultFileSystem().getRoot().getFileObject("ireport/templates/" + type);
        if (templatesFileObject == null) return;
        DataFolder templatesDataFolder = DataFolder.findFolder(templatesFileObject);
        if (templatesDataFolder == null) return;
        
        Enumeration<DataObject> enObj = templatesDataFolder.children();
        while (enObj.hasMoreElements())
        {
            DataObject dataObject = enObj.nextElement();
            if (dataObject.getPrimaryFile().getExt() == null ||
                dataObject.getPrimaryFile().getExt().length() == 0)
            {
                ((DefaultListModel)jList1.getModel()).addElement(dataObject);
            }
        }
        
        
        
        // Load all the templates from the templates directories (is set...)
        String pathsString = IReportManager.getPreferences().get(IReportManager.TEMPLATE_PATH, "");
        // All the paths are separated by an end line...
        if (pathsString.length() > 0)
        {
            String[] paths = pathsString.split("\\n");
            for (int i=0; i<paths.length; ++i)
            {
                File f = new File(paths[i]);
                loadTemplatesFromFile(f, type);
                
            }
        }
        
        
        if (((DefaultListModel)jList1.getModel()).getSize() > 0)
        {
            jList1.setSelectedIndex(0);
        }
    }

    public void loadTemplatesFromFile(File file, String type)
    {
        if (file == null || !file.exists()) return;
        
        if (file.isDirectory())
        {
            loadTemplatesFromDirectory(file, type);
            return;
        }
        
        final String ext = (type.equals("columnar")) ? "c.jrxml" : "t.jrxml";
        final String ext2 = (type.equals("columnar")) ? "c.xml" : "t.xml";
        
        if (file.getName().toLowerCase().endsWith(ext) ||
            file.getName().toLowerCase().endsWith(ext2))
        {
            FileObject fo = FileUtil.toFileObject(file);
            if (fo != null)
            {
                DataObject dobj = null;
                try {
                    dobj = DataObject.find(fo);
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
                if (dobj != null)
                {
                     ((DefaultListModel)jList1.getModel()).addElement(dobj);
                }
            }
        }
    }


    public void loadTemplatesFromDirectory(File folder, String type)
    {
        if (folder != null && folder.exists())
        {
            File[] files = folder.listFiles();

            for (int i=0; i<files.length; ++i)
            {
                loadTemplatesFromFile(files[i], type);
            }
        }
    }
    */

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton1, "Columnar Layout");
        jRadioButton1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton1StateChanged(evt);
            }
        });
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(50, 4, 0, 4);
        add(jRadioButton1, gridBagConstraints);

        buttonGroup1.add(jRadioButton2);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButton2, "Tabular Layout");
        jRadioButton2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jRadioButton2StateChanged(evt);
            }
        });
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        add(jRadioButton2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton1StateChanged
    }//GEN-LAST:event_jRadioButton1StateChanged

    private void jRadioButton2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jRadioButton2StateChanged
        
    }//GEN-LAST:event_jRadioButton2StateChanged

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        //updateTemplates();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        //updateTemplates();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    // End of variables declaration//GEN-END:variables

    public String getReportType()
    {
        return jRadioButton1.isSelected() ? "columnar" : "tabular";//NOI18N

    }
    
//    public FileObject getReportTemplate()
//    {
//        DataObject dataObject = (DataObject)jList1.getSelectedValue();
//        return dataObject.getPrimaryFile();
//    }
    
    
//    static class FieldListCellRenderer extends DefaultListCellRenderer
//    {
//
//        @Override
//        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//
//            if (value instanceof DataObject)
//            {
//                value = ((DataObject)value).getName();
//            }
//
//            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//        }
//
//    }
}

