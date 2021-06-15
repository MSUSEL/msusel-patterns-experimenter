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

import com.jaspersoft.ireport.designer.IRURLClassLoader;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.ReportClassLoader;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.connection.gui.EJBQLConnectionEditor;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author  Administrator
 */
public class EJBQLConnection extends IReportConnection {
    
    private String name;
    
    private java.util.HashMap map = null;
    private String persistenceUnit;
    
    private EntityManager em = null;
    private EntityManagerFactory emf = null;
    
    private int usedby = 0;
    
    /** Creates a new instance of JDBCConnection */
    
    
    public EJBQLConnection() {
        map = new java.util.HashMap();
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
    
    @Override
    public boolean isJRDataSource() {
        return false;
    }
    
    /*
     *  This method return all properties used by this connection
     */
    @Override
    public java.util.HashMap getProperties()
    {    
        return map;
    }
    
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.map = map;
    }
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    @Override
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource()
    { 
        return null;
    }
    
    public EntityManager getEntityManager() throws Exception 
    {           
            if (em == null)
            {
                if (emf == null)
                {
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    if (cl instanceof ReportClassLoader)
                    {
                        List items = ((ReportClassLoader)cl).getCachedItems();
                        
                        java.net.URL[] urls = new java.net.URL[items.size()];
                        for (int i=0; i<items.size(); ++i)
                        {
                            urls[i] = new java.io.File(""+items.get(i)).toURI().toURL();
                        }
                        IRURLClassLoader urlClassLoader = new IRURLClassLoader(urls,  cl );
                        Thread.currentThread().setContextClassLoader(urlClassLoader  );
                    }
                    
                    
                    emf = Persistence.createEntityManagerFactory( 
                            Misc.nvl(getProperties().get("PersistenceUnit"), null), new HashMap());
                    //if (emf == null) throw new Exception("Unable to create the EntityManagerFactory for persistence unit " + Misc.nvl(getProperties().get("PersistenceUnit"), null));
                    //Thread.currentThread().setContextClassLoader().
                }
                em = emf.createEntityManager();
            }
            usedby ++;
            return em;
    }
    
    public void closeEntityManager()
    {
        try {
            if (em != null)
            {
                usedby--;
                if (usedby == 0)
                {
                    em.close();
                    em = null;
                }
            }
        } catch (Exception ex)
        {
        }
    }   
    
    public String getDescription(){ return "EJBQL connection"; } //"connectionType.ejbql"
    
    
    @Override
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new EJBQLConnectionEditor();
    }
   
    @Override
    public void test() throws Exception
    {
        try {
                SwingUtilities.invokeLater( new Runnable()
                {
                    public void run()
                    {
                        
                        Thread.currentThread().setContextClassLoader( IReportManager.getInstance().getReportClassLoader() );

                        try {

                              getEntityManager();
                              closeEntityManager();
                              JOptionPane.showMessageDialog(Misc.getMainWindow(),
                                      //I18n.getString("messages.connectionDialog.connectionTestSuccessful",
                                      "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE);

                        } catch (Exception ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(Misc.getMainWindow(),
                                    ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                            return;					
                        } 
                        finally
                        {

                        }
                        
                    }
                });
            } catch (Exception ex)
            {}
    }
}
