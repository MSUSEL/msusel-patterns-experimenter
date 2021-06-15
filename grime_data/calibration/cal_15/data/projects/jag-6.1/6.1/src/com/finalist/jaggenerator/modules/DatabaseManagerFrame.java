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
package com.finalist.jaggenerator.modules;

import com.finalist.jaggenerator.Database;
import com.finalist.jaggenerator.DatabaseManager;
import com.finalist.jaggenerator.ExtensionsFileFilter;
import com.finalist.jaggenerator.JagGenerator;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.io.File;
import java.util.*;

/**
 * The JFrame GUI behind the DatabaseManager.
 *
 * @author  m.oconnor
 */
public class DatabaseManagerFrame extends javax.swing.JFrame {
   private static DatabaseManagerFrame ourInstance;
   private static final List newDatabases = new ArrayList();
   public static final String SELECT = "< select >";
   private static final String DRIVER_IMPORT_FILECHOOSER = "import driver";
   private static final String[] ACCEPTABLE_EXTENSIONS = new String[] {"class", "jar", "zip"};
   private static final Vector COLUMN_NAMES = new Vector(Arrays.asList(new String[]{
      "Database Type",
      "Driver File",
      "Driver Class",
      "Type Mapping",
   }));


   /**
    * Only one instance of the DatabaseManagerFrame is allowed - this method obtains the one and only instance.
    * @return
    */
   public synchronized static DatabaseManagerFrame getInstance() {
      if (ourInstance == null) {
         ourInstance = new DatabaseManagerFrame();
      }
      return ourInstance;
   }

   /**
    * Whenever the frame is shown, reinitialise the list.
    */
   public void show() {
      newDatabases.clear();
      refreshModel();
      super.show();
   }

   private DatabaseManagerFrame() {
      initComponents();
      postInit();
   }


   private void postInit() {
      this.setLocationRelativeTo(JagGenerator.jagGenerator);
      jTable1.setSurrendersFocusOnKeystroke(true);
      refreshModel();
   }

   private void refreshModel() {
      jTable1.setModel(new DefaultTableModel(getData(), COLUMN_NAMES) {
         //The middle two columns are not editable
         public boolean isCellEditable(int row, int column) {
            if (column == 1 || column == 2) {
               return false;
            }
            return super.isCellEditable(row, column);
         }
      });

      TableColumn typeMappingColumn = jTable1.getColumnModel().getColumn(3);
      jTable1.getModel().addTableModelListener(new TableModelListener() {
         //when the user selects a type mapping and hasn't yet set a DB name,
         //make the DB name to the same value as the type mapping..
         public void tableChanged(TableModelEvent e) {
            int column = e.getColumn();
            if (column != 3) return; //only interested in user selecting a typemapping

            int row = e.getFirstRow();
            TableModel model = (TableModel) e.getSource();
            String dbName = (String) model.getValueAt(row, 0);
            if (!Database.ENTER_DB_NAME.equals(dbName)) return;
            String typeMapping = (String) model.getValueAt(row, column);
            model.setValueAt(typeMapping, row, 0);
         }
      });

      JComboBox comboBox = new JComboBox();
      comboBox.addItem(SELECT);
      String[] typeMappings = DatabaseManager.getInstance().getTypeMappings();
      for (int i = 0; i < typeMappings.length; i++) {
         comboBox.addItem(typeMappings[i]);
      }

      typeMappingColumn.setCellEditor(new DefaultCellEditor(comboBox));
   }

   private Vector getData() {
      Vector data = new Vector();
      ArrayList temp = new ArrayList();
      temp.addAll(Arrays.asList(DatabaseManager.getInstance().getSupportedDatabases()));
      temp.addAll(newDatabases);
      Iterator dbs = temp.iterator();
      while (dbs.hasNext()) {
         Database db = (Database) dbs.next();
         Vector vickie = new Vector();
         vickie.add(db.getDbName());
         vickie.add(db.getFilename());
         vickie.add(db.getDriverClass());
         vickie.add(db.getTypeMapping());
         data.add(vickie);
      }

      return data;
   }


   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        addButtonPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        cancelButtonPanel = new javax.swing.JPanel();
        CancelButton = new javax.swing.JButton();
        saveButtonPanel = new javax.swing.JPanel();
        OKButton = new javax.swing.JButton();

        setTitle("JAG Database Driver Manager");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jScrollPane1.setPreferredSize(new java.awt.Dimension(700, 300));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("JAG Supported Databases");
        jLabel1.setMaximumSize(new java.awt.Dimension(149, 32));
        jLabel1.setMinimumSize(new java.awt.Dimension(149, 32));
        jLabel1.setPreferredSize(new java.awt.Dimension(149, 32));
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        getContentPane().add(jLabel1, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        addButton.setText("Add new JDBC driver..");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        addButtonPanel.add(addButton);

        jPanel1.add(addButtonPanel, java.awt.BorderLayout.CENTER);

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        cancelButtonPanel.add(CancelButton);

        jPanel1.add(cancelButtonPanel, java.awt.BorderLayout.WEST);

        OKButton.setText("Save");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });

        saveButtonPanel.add(OKButton);

        jPanel1.add(saveButtonPanel, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
      JagGenerator.logToConsole("Importing new database driver(s).  Please wait...");
      final JFileChooser fileChooser =
            new JFileChooser(JagGenerator.getFileChooserStartDir(DRIVER_IMPORT_FILECHOOSER));
      ExtensionsFileFilter filter = new ExtensionsFileFilter(ACCEPTABLE_EXTENSIONS);
      fileChooser.setDialogTitle("JDBC Driver Import: Choose a class / archive file..");
      fileChooser.setFileFilter(filter);
      int fileChooserStatus = fileChooser.showOpenDialog(null);
      if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
         File driverFile = fileChooser.getSelectedFile();
         newDatabases.addAll(DatabaseManager.getInstance().addDrivers(driverFile));
         JagGenerator.logToConsole("...Driver import complete.");
         JagGenerator.setFileChooserStartDir(DRIVER_IMPORT_FILECHOOSER, driverFile);
         refreshModel();

      } else {
         JagGenerator.logToConsole("...aborted!");
      }
    }//GEN-LAST:event_addButtonActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        hide();
    }//GEN-LAST:event_CancelButtonActionPerformed

   private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKButtonActionPerformed
      //We have to explicitly tell the table's cell editor to accept all 'partial' typed-in values
      //(i.e. the value contained by the cell that was being edited when the OK button was pressed..
      if (jTable1.getCellEditor() != null) {
         jTable1.getCellEditor().stopCellEditing();
      }
      ArrayList databases = new ArrayList();
      for (int row = 0; row < jTable1.getRowCount(); row++) {
         Database db = new Database();
         String dbName = (String) jTable1.getModel().getValueAt(row, 0);
         db.setDbName(dbName);
         db.setFilename((String) jTable1.getModel().getValueAt(row, 1));
         db.setDriverClass((String) jTable1.getModel().getValueAt(row, 2));

         String mapping = (String) jTable1.getModel().getValueAt(row, 3);
         if (SELECT.equals(mapping)) {
            JOptionPane.showMessageDialog(this,
                  "Please select a mapping for the new database driver from the drop-down list.",
                  "Missing Type Mapping!!", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
         }

         db.setTypeMapping(mapping);
         databases.add(db);
      }

      DatabaseManager.getInstance().setDatabases(databases);
      hide();

   }//GEN-LAST:event_OKButtonActionPerformed

   /** Exit the Application */
   private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
      this.hide();
   }//GEN-LAST:event_exitForm

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton OKButton;
    private javax.swing.JButton addButton;
    private javax.swing.JPanel addButtonPanel;
    private javax.swing.JPanel cancelButtonPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel saveButtonPanel;
    // End of variables declaration//GEN-END:variables

}
