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
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.connection.gui.JRHibernateConnectionEditor;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.classic.Session;
/**
 *
 * @author  Administrator
 */
public class JRHibernateConnection extends IReportConnection {
    
    private String name;
    private boolean useAnnotations = true;
    
    /** Creates a new instance of JRHibernateConnection */   
    public JRHibernateConnection() {
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
        java.util.HashMap map = new java.util.HashMap();
        map.put("useAnnotations", ""+useAnnotations);
        return map;
    }
    
    @Override
    public void loadProperties(java.util.HashMap map)
    {
        String b = (String)map.get("useAnnotations");
        if (b!= null) useAnnotations = Boolean.valueOf(b);
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
        return null;
    }
    
    public Session createSession() throws org.hibernate.HibernateException
    {
         return getSessionFactory().openSession(); 
    }

    public SessionFactory getSessionFactory() throws org.hibernate.HibernateException {

          if (useAnnotations)
          {
            AnnotationConfiguration conf = new org.hibernate.cfg.AnnotationConfiguration().configure();
            conf.setProperty(Environment.CONNECTION_PROVIDER, "com.jaspersoft.ireport.designer.connection.HibernateConnectionProvider");
            return conf.buildSessionFactory();
          }
          else
          {
             return new Configuration().configure().buildSessionFactory();
          }
    }
    
    public String getDescription(){ return "Hibernate connection"; } //"connectionType.hibernate"
    
    @Override
    public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRHibernateConnectionEditor();
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
                        SessionFactory hb_sessionFactory = null;
                        try {
                            hb_sessionFactory = getSessionFactory();
                            
                            // Try to execute an hibernate query...
                            Session hb_session = hb_sessionFactory.openSession();
                            Transaction  transaction = hb_session.beginTransaction();

                            Query q = hb_session.createQuery("from java.lang.String s");
                        
                            q.setFetchSize(100);
                            java.util.Iterator iterator = q.iterate();
                            // this is a stupid thing: iterator.next();

                            while (iterator.hasNext())
                            {
                                Object obj = iterator.next();
                            }
                            
                            JOptionPane.showMessageDialog(Misc.getMainWindow(),
                                    //I18n.getString("messages.connectionDialog.connectionTestSuccessful",
                                    "Connection test successful!","",JOptionPane.INFORMATION_MESSAGE);

                        } catch (Throwable ex)
                        {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(Misc.getMainWindow(),ex.getMessage(),
                                    "Error",JOptionPane.ERROR_MESSAGE);
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

    /**
     * @return the useAnnotations
     */
    public boolean isUseAnnotations() {
        return useAnnotations;
    }

    /**
     * @param useAnnotations the useAnnotations to set
     */
    public void setUseAnnotations(boolean useAnnotations) {
        this.useAnnotations = useAnnotations;
    }
}

