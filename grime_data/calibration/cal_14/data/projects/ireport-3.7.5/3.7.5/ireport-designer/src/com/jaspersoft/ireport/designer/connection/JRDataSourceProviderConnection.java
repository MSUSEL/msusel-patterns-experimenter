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
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.gui.JRDataSourceProviderConnectionEditor;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.*;
import javax.swing.*;
import org.openide.util.Mutex;


/**
 *
 * @author  Administrator
 */
public class JRDataSourceProviderConnection extends IReportConnection {
    
    
    private net.sf.jasperreports.engine.JRDataSourceProvider dsp;
    private net.sf.jasperreports.engine.JRDataSource ds;
    private java.util.HashMap properties = new java.util.HashMap();
    
    public  net.sf.jasperreports.engine.JRDataSourceProvider getDataSourceProvider() {
        
        if (dsp == null && this.getProperties().get("JRDataSourceProvider") != null)
        {
            try {
                dsp = (JRDataSourceProvider)(Class.forName( (String)this.getProperties().get("JRDataSourceProvider"), true, IReportManager.getInstance().getReportClassLoader()).newInstance());
            } catch (NoClassDefFoundError ex)
	    {
                showErrorMessage(
                                //I18n.getString("messages.JRDataSourceProviderConnection.noClassDefFoundError",
                                "No class definition found error!!\nCheck your classpath!",
                                "Exception"); //I18n.getString("message.title.exception",
            } 
            catch (ClassNotFoundException ex)
            {
            showErrorMessage(
                        //I18n.getString("messages.JRDataSourceProviderConnection.classNotFoundError",
                                "Class not found error!!\nCheck your classpath!",
                        "Exception"); //I18n.getString("message.title.exception",
            } 
            catch (Exception ex)
            {

                showErrorMessage( I18n.getString("unexpected.datasource.error"  ,ex.getMessage(), Misc.getLogFile()),
                        "Exception"); //I18n.getString("message.title.exception",
            }
        }
        
        return dsp;
    }
    
    
     
    
    /** Creates a new instance of JRDataSourceProviderConnection */
    public JRDataSourceProviderConnection() {
    }
    
        /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { 
        
        return getJRDataSource(null);
    }
    
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource(JasperReport jasper) { 
        
        if (ds != null)
        {
            JOptionPane.showMessageDialog(Misc.getMainWindow(),"This datasource is already in use by another filling process!!", //I18n.getString("messages.JRDataSourceProviderConnection.datasourceInUse"
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        try {
            ds = getDataSourceProvider().create(jasper);
        } catch (final Exception ex)
        {
            Mutex.EVENT.readAccess(new Runnable() {

                public void run() {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                    Misc.formatString("Problems occurred creating the new datasource!!\n{0}", new Object[]{""+ex.getMessage()}), //"messages.JRDataSourceProviderConnection.problemsCreatingDatasource"
                    "Error",JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        
        return ds;
    }
    
    public void disposeDataSource()
    {
            if (dsp != null)
            {
                try {
                    dsp.dispose(ds);
                } catch (Exception ex) {}
                ds = null;
            }
    }
    
    public java.util.HashMap getProperties()
    {
        return properties;
    }
    
    /** All properties of a IReportConnection are stored in a XML file as Pair key/value
     *  This HashMap contains alla properties found for this IReportConnection in the
     *  XML. You must use this hashMap to initialize all attributes of your IReprotConnection
     */
    public void loadProperties(java.util.HashMap map)
    {
        properties = map;
    }
    
    /** Redefine this method is not useful (and not raccomended)
     *  It just write a portion of XML for save properties a IReportConnection name
     */
    public void save(java.io.PrintWriter pw)
    {
        java.util.HashMap hm = this.getProperties();
        pw.println("\t<iReportConnection name=\""+ this.getName() +"\" connectionClass=\"" + this.getClass().getName() +"\">");
        java.util.Iterator iterator = hm.keySet().iterator();
        
        while (iterator.hasNext())
        {
            String key = (String)iterator.next();
            pw.println("\t\t<connectionParameter name=\""  +  key + "\"><![CDATA[" + hm.get(key) + "]]></connectionParameter>");
        }
        pw.println("\t</iReportConnection>");
    }    
    
    public String getDescription(){ return "JRDataSourceProvider"; } //"connectionType.datasourceProvider"
    
    
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRDataSourceProviderConnectionEditor();
    }
    
    public void test() throws Exception
    {
        try {
                
                Class c = Class.forName((String)this.getProperties().get("JRDataSourceProvider") , true, IReportManager.getInstance().getReportClassLoader());

                if (!(net.sf.jasperreports.engine.JRDataSourceProvider.class.isAssignableFrom(c)))
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString("\"{0}\" is not a subclass of\nnet.sf.jasperreports.engine.JRDataSourceProvider.",  //"messages.connectionDialog.invalidJRDataSourceProvider"
                            new Object[]{this.getProperties().get("JRDataSourceProvider")}),
                            "Error",JOptionPane.ERROR_MESSAGE);
                    return;	
                }
                else
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE); //"messages.connectionDialog.connectionTestSuccessful"
                    return;
                }
                
            } catch (NoClassDefFoundError ex)
            {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString( //"messages.connection.noClassDefFoundError",
                            "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                            new Object[]{"", ""+ex.getMessage()}),
                            "Exception",JOptionPane.ERROR_MESSAGE);
                    return;					
            } 
            catch (ClassNotFoundException ex)
            {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString( //"messages.connection.classNotFoundError",
                            "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                            new Object[]{"", ""+ex.getMessage(), "" + this.getProperties().get("JRDataSourceProvider")}),
                            "Exception",JOptionPane.ERROR_MESSAGE);
                    return;				
            } 
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(Misc.getMainWindow(),
                    Misc.formatString( //"messages.connection.generalError2",
                    "{0}\nGeneral problem:\n {1}",
                    new Object[]{"", ""+ex.getMessage()}),
                    "Exception",JOptionPane.ERROR_MESSAGE);
                    return;									
            }
    }
}
