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
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.gui.JDBCNBConnectionEditor;
import com.jaspersoft.ireport.designer.data.WizardFieldsProvider;
import com.jaspersoft.ireport.designer.data.fieldsproviders.SQLFieldsProvider;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.netbeans.api.db.explorer.ConnectionManager;
import org.netbeans.api.db.explorer.DatabaseConnection;

/**
 *
 * @author  Administrator
 */
public class JDBCNBConnection extends com.jaspersoft.ireport.designer.IReportConnection implements WizardFieldsProvider {
    
    private String name;
    private String url;
    
    public JDBCNBConnection() {
    }


    public DatabaseConnection getDatabaseConnectionObject()
    {
        return ConnectionManager.getDefault().getConnection(url);
    }

    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    @Override
    public java.sql.Connection getConnection() {
        
            // Try the java connection...
            Connection c = null;
            try {
                
                    final DatabaseConnection dbconn = ConnectionManager.getDefault().getConnection(url);
                    if (dbconn == null)
                    {
                        throw new Exception("Connection " + url + " not found.");
                    }
                    
                    c = dbconn.getJDBCConnection();
                    
                    if (c == null)
                    {
                        if (SwingUtilities.isEventDispatchThread())
                        {
                            ConnectionManager.getDefault().showConnectionDialog(dbconn);
                        }
                        else
                        {
                            SwingUtilities.invokeAndWait(new Runnable() {

                                public void run() {
                                    ConnectionManager.getDefault().showConnectionDialog(dbconn);
                                }
                            });
                        }

                        c = dbconn.getJDBCConnection();
                        
                    }
                    
                    if (c == null)
                    {
                        throw new Exception("Unable to connect.");
                    }
            
            } 
            catch (Exception ex)
            {
                ex.printStackTrace();
                
                showErrorMessage(Misc.formatString( // "messages.connection.generalError",
                            "{0}\nGeneral problem: {1}\nPlease check your username and password. The DBMS is running?!",
                            new Object[]{""+ url, ""+ex.getMessage()}),
                            "Exception", ex);
            }
            return c;
    }    
    
    private void  showErrorMessage(String errorMsg, String title, Throwable theException)
    {
        
        final JXErrorPane pane = new JXErrorPane();
        //pane.setLocale(I18n.getCurrentLocale());
       
        String[] lines = errorMsg.split("\r\n|\n|\r");

        String shortMessage = errorMsg;
        if (lines.length > 4)
        {
            shortMessage = "";
            for (int i=0; i<4; ++i)
            {
                shortMessage += lines[i]+"\n";
            }
            shortMessage = shortMessage.trim() + "\n...";
        }
      
        final ErrorInfo ei = new ErrorInfo(title,
                 shortMessage,
                 null, //"<html><pre>" + errorMsg + "</pre>"
                 null,
                 theException,
                 null,
                 null);
         
        
        /*
        
        
        final String fErrorMsg = errorMsg;
        */
        Runnable r = new Runnable() {
                public void run() {
                   // JOptionPane.showMessageDialog(MainFrame.getMainInstance(),fErrorMsg,title,JOptionPane.ERROR_MESSAGE);
                
                    pane.setErrorInfo(ei);
                   JXErrorPane.showDialog(Misc.getMainWindow(), pane);
                }
            };

        if (!SwingUtilities.isEventDispatchThread())
        {
            try {
                SwingUtilities.invokeAndWait( r );
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        else
        {
                r.run();
        }
    }
    
    
    /*  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     *
     */
    @Override
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() {
        return  new net.sf.jasperreports.engine.JREmptyDataSource();
    }
    
    @Override
    public boolean isJDBCConnection() {
        return true;
    }
    
       
    /** Getter for property url.
     * @return Value of property url.
     *
     */
    public java.lang.String getUrl() {
        return url;
    }
    
    /** Setter for property url.
     * @param url New value of property url.
     *
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }
    
 
    
    /*
     *  This method return all properties used by this connection
     */
    @SuppressWarnings("unchecked")
    @Override
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("Url", Misc.nvl(this.getUrl(),""));
        return map;
    }
    
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.setUrl( (String)map.get("Url"));
    }
    
    
    public String getDescription(){ return "NetBeans Database JDBC connection"; } //"connectionType.jdbc"
    
    @Override
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JDBCNBConnectionEditor();
    }
    
    
    @Override
    public void test() throws Exception
    {
        // Try the java connection...
        Connection conn = null;
        ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(IReportManager.getReportClassLoader());
        conn = getConnection();
        if (conn == null) throw new Exception("");
        Thread.currentThread().setContextClassLoader(oldCL);
        JOptionPane.showMessageDialog(Misc.getMainWindow(),"Connection test successful!","",JOptionPane.INFORMATION_MESSAGE); //"messages.connectionDialog.connectionTestSuccessful"
        return;
    }
    
    public String getQueryLanguage() {
        return "SQL";
    }

    public List<JRDesignField> readFields(String query) throws Exception {
        
        SQLFieldsProvider provider = new SQLFieldsProvider();
        List<JRDesignField> result = new ArrayList<JRDesignField>();
        JRDesignDataset dataset = new JRDesignDataset(true);
        JRDesignQuery dquery = new JRDesignQuery();
        dquery.setLanguage("SQL");
        dquery.setText(query);
        dataset.setQuery(dquery);
        JRField[] fields = provider.getFields(this, dataset, new HashMap());
        for (int i=0; i<fields.length; ++i)
        {
            result.add((JRDesignField)fields[i]);
        }
        
        return result;
    }

    public boolean supportsDesign() {
        return true;
    }

    public String designQuery(String query) {
        
        try {
            SQLFieldsProvider provider = new SQLFieldsProvider();
            return provider.designQuery(this, query, null);
        } catch (Exception ex)
        {
            return query;
        }
    }
}
