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
package com.jaspersoft.ireport.designer.connection;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.connection.gui.CSVDataSourceConnectionEditor;
import com.jaspersoft.ireport.designer.data.WizardFieldsProvider;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;

/**
 *
 * @author  Administrator
 */
public class JRCSVDataSourceConnection extends IReportConnection implements WizardFieldsProvider {
    
    private String name;
    private String recordDelimiter = "\n";
    private String fieldDelimiter = ",";
    private boolean useFirstRowAsHeader = false;
    private String customDateFormat = "";
        
    private String filename;
    
    private Vector columnNames = new Vector();
    
    /** Creates a new instance of JDBCConnection */   
    public JRCSVDataSourceConnection() {
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    @Override
    public java.sql.Connection getConnection() {       
            return null;
    }
    
    @Override
    public boolean isJDBCConnection() {
        return false;
    }
    
    /*
     *  This method return all properties used by this connection
     */
    @SuppressWarnings("unchecked")
    @Override
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("Filename", Misc.nvl(this.getFilename() ,"") );    
        map.put("recordDelimiter", Misc.nvl(  Misc.addSlashesString(this.getRecordDelimiter()),"\n") );
        map.put("fieldDelimiter", Misc.nvl( Misc.addSlashesString(this.getFieldDelimiter()) ,"") );
        map.put("useFirstRowAsHeader", Misc.nvl(""+this.isUseFirstRowAsHeader() ,"") );
        map.put("customDateFormat", Misc.nvl(this.getCustomDateFormat() ,"") );
        
        for (int i=0; i< getColumnNames().size(); ++i)
        {
            map.put("COLUMN_" + i,getColumnNames().elementAt(i) );
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.setFilename( (String)map.get("Filename"));
        String fieldDelimiterStr = (String)map.get("fieldDelimiter");
        if (fieldDelimiterStr != null)
        {
            this.setFieldDelimiter( Misc.removeSlashesString(fieldDelimiterStr) );
        }
        String recordDelimiterStr = (String)map.get("recordDelimiter");
        if (recordDelimiterStr != null)
        {
            this.setRecordDelimiter( Misc.removeSlashesString(recordDelimiterStr) );
        }
        this.setUseFirstRowAsHeader( ((String)map.get("useFirstRowAsHeader")).equals("true"));
        this.setCustomDateFormat( (String)map.get("customDateFormat"));
        
        int i = 0;
        while (map.containsKey("COLUMN_" + i))
        {
           getColumnNames().add( map.get("COLUMN_" + i));
           i++;
        }
        
    }
    
    /**
     * Getter for property filename.
     * @return Value of property filename.
     */
    public java.lang.String getFilename() {
        return filename;
    }    
   
    /**
     * Setter for property filename.
     * @param filename New value of property filename.
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }    
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    @Override
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    @Override
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    @Override
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { 
        
        try {
        JRCsvDataSource ds = new JRCsvDataSource(new File(getFilename()));
        if (this.getCustomDateFormat() != null && this.getCustomDateFormat().length() > 0)
        {
            ds.setDateFormat(new SimpleDateFormat(this.getCustomDateFormat()));
        }
        
        ds.setFieldDelimiter( getFieldDelimiter().charAt(0) );
        ds.setRecordDelimiter( getRecordDelimiter());
        ds.setUseFirstRowAsHeader( isUseFirstRowAsHeader());
        
        if (!isUseFirstRowAsHeader())
        {
            String[] names = new String[getColumnNames().size()];
            for (int i=0; i<names.length; ++i )
            {
                names[i] = ""+getColumnNames().elementAt(i);
            }
            ds.setColumnNames( names );
        }
        
        return ds;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return super.getJRDataSource();
        }
    }

    public boolean isUseFirstRowAsHeader() {
        return useFirstRowAsHeader;
    }

    public void setUseFirstRowAsHeader(boolean useFirstRowAsHeader) {
        this.useFirstRowAsHeader = useFirstRowAsHeader;
    }

    public String getRecordDelimiter() {
        return recordDelimiter;
    }

    public void setRecordDelimiter(String recordDelimiter) {
        this.recordDelimiter = recordDelimiter;
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(String fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public String getCustomDateFormat() {
        return customDateFormat;
    }

    public void setCustomDateFormat(String customDateFormat) {
        this.customDateFormat = customDateFormat;
    }

    public Vector getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(Vector columnNames) {
        this.columnNames = columnNames;
    }
    
    public String getDescription(){ return "File CSV datasource"; } //"connectionType.csv"
    
    @Override
        public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new CSVDataSourceConnectionEditor();
    }
        
    @Override
    public void test() throws Exception 
    {
            String csv_file = getFilename();
            
            try {
                
                JRCSVDataSourceConnection con = new JRCSVDataSourceConnection();
                java.io.File f = new java.io.File(csv_file);
                if (!f.exists())
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                         Misc.formatString("File {0} not found", new Object[]{csv_file}), //"messages.connectionDialog.fileNotFound"
                         "Error",JOptionPane.ERROR_MESSAGE); //"message.title.error"
                    return;	
                }
                
                con.setFilename( csv_file );
                if (con.getJRDataSource() != null)
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE); //"messages.connectionDialog.connectionTestSuccessful"
                    return;
                }
                
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(Misc.getMainWindow(),
                        ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); //"message.title.error"
		ex.printStackTrace();
                return;	
            }        
    }

    public String getQueryLanguage() {
        return null;
    }

    public List<JRDesignField> readFields(String query) throws Exception {
        
        List<JRDesignField> fields = new ArrayList<JRDesignField>();

        Vector names = getColumnNames();

        for (int nd =0; nd < names.size(); ++nd) {
            String fieldName = ""+names.elementAt(nd);
            JRDesignField field = new JRDesignField();
            field.setName(fieldName);
            field.setValueClassName("java.lang.String");
            //field.setDescription(null); //Field returned by " +methods[i].getName() + " (real type: "+ returnType +")");
            fields.add(field);
        }
        return  fields;
    }

    public boolean supportsDesign() {
        return false;
    }

    public String designQuery(String query) {
        return query;
    }
    
}

