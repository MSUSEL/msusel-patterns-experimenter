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
import com.jaspersoft.ireport.designer.connection.gui.JRCustomDataSourceConnectionEditor;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import javax.swing.JOptionPane;

/**
 *
 * @author  Administrator
 */
public class JRCustomDataSourceConnection extends IReportConnection {
    
    private String factoryClass;
    
    private String methodToCall;
    
    /** Creates a new instance of JDBCConnection */
    
    
    public JRCustomDataSourceConnection() {
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
        map.put("FactoryClass", Misc.nvl(this.getFactoryClass() ,"") );
        map.put("MethodToCall", Misc.nvl(this.getMethodToCall(),""));
       
        return map;
    }
    
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.setFactoryClass( (String)map.get("FactoryClass"));
        this.setMethodToCall( (String)map.get("MethodToCall"));
    }
    

    
    /** Getter for property methodToCall.
     * @return Value of property methodToCall.
     *
     */
    public java.lang.String getMethodToCall() {
        return methodToCall;
    }
    
    /** Setter for property methodToCall.
     * @param methodToCall New value of property methodToCall.
     *
     */
    public void setMethodToCall(java.lang.String methodToCall) {
        this.methodToCall = methodToCall;
    }
    
    /** Getter for property factoryClass.
     * @return Value of property factoryClass.
     *
     */
    public java.lang.String getFactoryClass() {
        return factoryClass;
    }
    
    /** Setter for property factoryClass.
     * @param factoryClass New value of property factoryClass.
     *
     */
    public void setFactoryClass(java.lang.String factoryClass) {
        this.factoryClass = factoryClass;
    }
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    @Override
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        try {
            Class clazz = Class.forName( factoryClass, true, Thread.currentThread().getContextClassLoader() );
            return (net.sf.jasperreports.engine.JRDataSource) clazz.getMethod( methodToCall, new Class[0]).invoke(null,new Object[0]);
        } catch (Throwable ex)
        {
            showErrorMessage( I18n.getString("unexpected.datasource.error"  ,ex.getMessage(), Misc.getLogFile()),
                        "Exception"); //I18n.getString("message.title.exception",

            ex.printStackTrace();
            return super.getJRDataSource();
        }
    }
    
    public String getDescription(){ return "Custom JRDataSource"; } //"connectionType.customDataSource"

    @Override
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRCustomDataSourceConnectionEditor();
    }
    
    @Override
    public void test() throws Exception
    {
        try {
                Object obj = Class.forName( getFactoryClass() , true, IReportManager.getInstance().getReportClassLoader()).newInstance();
                obj.getClass().getMethod( getMethodToCall() , new Class[0]).invoke(obj,new Object[0]);                
        }catch (NoClassDefFoundError ex)
        {
                JOptionPane.showMessageDialog(Misc.getMainWindow(),
                        Misc.formatString( //"messages.connection.noClassDefFoundError"
                        "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                        new Object[]{"", ""+ex.getMessage()}), 
                        "Exception",JOptionPane.ERROR_MESSAGE); //"message.title.exception"
                throw new Exception();					
        } 
        catch (ClassNotFoundException ex)
        {
                JOptionPane.showMessageDialog(Misc.getMainWindow(),
                         Misc.formatString( //"messages.connection.classNotFoundError",
                        "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                        new Object[]{"", ""+ex.getMessage(), "" + getFactoryClass()}),
                        "Exception",JOptionPane.ERROR_MESSAGE); //"message.title.exception"
                throw new Exception();				
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        JOptionPane.showMessageDialog(Misc.getMainWindow(),
                Misc.formatString( //"messages.connection.generalError2",
                "{0}\nGeneral problem:\n {1}",
                new Object[]{"", ""+ex.getMessage()}),
                "Exception",JOptionPane.ERROR_MESSAGE); //"message.title.exception"
                return;									
        }
        JOptionPane.showMessageDialog(Misc.getMainWindow(),
                "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE); //"messages.connectionDialog.connectionTestSuccessful"
        return;
    }
}
