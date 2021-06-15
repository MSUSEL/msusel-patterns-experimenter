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
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.gui.JavaBeanDataSourceConnectionEditor;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.sql.*;
import javax.swing.*;

/**
 *
 * @author  Administrator
 */
public class JavaBeanDataSourceConnection extends IReportConnection {
    
    public static String BEAN_ARRAY = "BEAN_ARRAY";
    public static String BEAN_COLLECTION = "BEAN_COLLECTION";
    
    private String factoryClass;
    
    private String methodToCall;
    
    private boolean useFieldDescription;
    
    private String type = "BEAN_COLLECTION";
    
    /** Creates a new instance of JDBCConnection */
    
    
    public JavaBeanDataSourceConnection() {
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
    @Override
    @SuppressWarnings("unchecked")
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("FactoryClass", Misc.nvl(this.getFactoryClass() ,"") );
        map.put("MethodToCall", Misc.nvl(this.getMethodToCall(),""));
        map.put("Type", Misc.nvl(this.getType(),""));
        map.put("UseFieldDescription", ""+this.isUseFieldDescription());
       
        return map;
    }
    
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.setFactoryClass( (String)map.get("FactoryClass"));
        this.setMethodToCall( (String)map.get("MethodToCall"));
        if (map.containsKey("UseFieldDescription"))
        {
            this.setUseFieldDescription( ((String)map.get("UseFieldDescription")).equals("true") );
        }
        if (map.containsKey("Type"))
        {
            this.setType( (String)map.get("Type"));
        }
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
     * Getter for property type.
     * @return Value of property type.
     */
    public java.lang.String getType() {
        return type;
    }    
   
    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }    
    
        /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    @Override
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        try {
            
                Class clazz = Thread.currentThread().getContextClassLoader().loadClass(factoryClass);
	        Object obj = clazz.newInstance();
       		Object return_obj = obj.getClass().getMethod( methodToCall, new Class[0]).invoke(null,new Object[0]);   
       		
       		if (return_obj != null) 
       		{
	       		if (Misc.nvl(this.getType(),"").equals(BEAN_ARRAY) )
	       		{			
       				return new net.sf.jasperreports.engine.data.JRBeanArrayDataSource((Object[]) return_obj, isUseFieldDescription());
                        }
       			else if (Misc.nvl(this.getType(),"").equals(BEAN_COLLECTION) )
       			{
       				return new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource((java.util.Collection) return_obj, isUseFieldDescription());	
       			}
       		}
       		return new net.sf.jasperreports.engine.JREmptyDataSource();
       		             
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return super.getJRDataSource();
        }
    }

    public boolean isUseFieldDescription() {
        return useFieldDescription;
    }

    public void setUseFieldDescription(boolean useFieldDescription) {
        this.useFieldDescription = useFieldDescription;
    }
    
    public String getDescription(){ return "JavaBeans set datasource"; } //"connectionType.javabeans"
    
    @Override
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JavaBeanDataSourceConnectionEditor();
    }
    
    @Override
    public void test() throws Exception 
    {
            try {
                Object obj = Class.forName(getFactoryClass(), true, IReportManager.getInstance().getReportClassLoader()).newInstance();
                Object ret_obj = obj.getClass().getMethod( getMethodToCall(), new Class[0]).invoke(null,new Object[0]);                
            
                if (ret_obj != null && getType().equals(BEAN_COLLECTION) && (ret_obj instanceof  java.util.Collection))
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            //I18n.getString("messages.connectionDialog.connectionTestSuccessful",
                            "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE);
                }
                else if (ret_obj != null  && getType().equals(BEAN_ARRAY) && (ret_obj instanceof  Object[]))
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            //I18n.getString("messages.connectionDialog.connectionTestSuccessful",
                            "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            //I18n.getString("messages.connectionDialog.notValidValueReturned",
                            "The method don't return a valid Array or java.util.Collection!\n",
                            "Error",JOptionPane.ERROR_MESSAGE);
                }
                
            }catch (NoClassDefFoundError ex)
            {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString( //"messages.connection.noClassDefFoundError",
                            "{0}\nNoClassDefFoundError!!\nCheck your classpath!\n{1}",
                            new Object[]{"", ""+ex.getMessage()}),
                            "Exception",JOptionPane.ERROR_MESSAGE);
                    throw ex;					
            } 
            catch (ClassNotFoundException ex)
            {
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                            Misc.formatString( //"messages.connection.classNotFoundError",
                            "{0}\nClassNotFoundError!\nMsg: {1}\nPossible not found class: {2}\nCheck your classpath!",
                            new Object[]{"", ""+ex.getMessage(), "" + getFactoryClass()}),
                            "Exception",JOptionPane.ERROR_MESSAGE);
                    throw ex;				
            } 
            catch (Exception ex)
            {
            	ex.printStackTrace();
            	
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),
                        Misc.formatString( //"messages.connection.generalError2",
                        "{0}\nGeneral problem:\n {1}",
                        new Object[]{"", ""+ex.getMessage()}),
                        "Exception",JOptionPane.ERROR_MESSAGE);
                throw ex;									
            }
    }
}
