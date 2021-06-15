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
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.designer.connection.gui.JRSpringLoadedHibernateConnectionEditor;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import org.hibernate.Query;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * 
 * @author Jeffrey Payne
 *
 */

public class JRSpringLoadedHibernateConnection extends JRHibernateConnection {
	
	private final static String PROP_KEY_SPRING_CONFIG = "spring.loaded.hibernate.spring.config";
	private final static String PROP_KEY_SESSION_FACTORY_ID = "spring.loaded.hibernate.session.factory.id";
	
	private String springConfig = null;
	private String sessionFactoryBeanId = null;
	

	
	public ApplicationContext getApplicationContext() {
		
                StringTokenizer parser = new StringTokenizer(getSpringConfig(), ",");
                String[] configs = new String[parser.countTokens()];
                int iCount = 0;
                while (parser.hasMoreTokens()) {
                        configs[iCount++] = parser.nextToken();
                }
                return new ClassPathXmlApplicationContext(configs);
	}

	public String getSessionFactoryBeanId() {
		return sessionFactoryBeanId;
	}


	public void setSessionFactoryBeanId(String sessionFactoryBeanId) {
		this.sessionFactoryBeanId = sessionFactoryBeanId;
	}


	public String getSpringConfig() {
		return springConfig;
	}


	public void setSpringConfig(String springConfig) {
		this.springConfig = springConfig;
	}
	
	 @Override
        public SessionFactory getSessionFactory() {
		 
		 return (SessionFactory)getApplicationContext().getBean(getSessionFactoryBeanId());
		 
	 }
	 
        /*
         *  This method return all properties used by this connection
         */
        @SuppressWarnings("unchecked")
        @Override
        public java.util.HashMap getProperties()
        {    
            java.util.HashMap map = new java.util.HashMap();
            map.put(PROP_KEY_SESSION_FACTORY_ID, getSessionFactoryBeanId());
            map.put(PROP_KEY_SPRING_CONFIG, getSpringConfig());
            return map;
        }

        @Override
        public void loadProperties(java.util.HashMap map)
        {
            setSessionFactoryBeanId((String)map.get(PROP_KEY_SESSION_FACTORY_ID));
            setSpringConfig((String)map.get(PROP_KEY_SPRING_CONFIG));
        }
        
        @Override
        public String getDescription(){ return "Spring loaded Hibernate connection"; } //"connectionType.hibernateSpring"
	
        
        @Override
        public IReportConnectionEditor getIReportConnectionEditor()
        {
            return new JRSpringLoadedHibernateConnectionEditor();
        }
         
        
        @Override
        public void test() throws Exception
        {
            try {
                    Thread.currentThread().setContextClassLoader( IReportManager.getInstance().getReportClassLoader() );
                    
                    SessionFactory sf = getSessionFactory();
                    if (sf == null) {
                            JOptionPane.showMessageDialog(Misc.getMainWindow(),
                                    //I18n.getString("messages.connectionDialog.noSessionFactoryReturned",
                                    "No session factory returned.  Check your session factory bean id against the spring configuration.",
                                    "Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        
                        
                            Session hb_session = sf.openSession();
                            Transaction  transaction = hb_session.beginTransaction();
                            Query q = hb_session.createQuery("select address as address Address as address");
                        
                            q.setFetchSize(1);
                            java.util.Iterator iterator = q.iterate();
                            // this is a stupid thing: iterator.next();

                            String[] aliases = q.getReturnAliases();
                            Type[] types = q.getReturnTypes();
                
                            
                        JOptionPane.showMessageDialog(Misc.getMainWindow(),
                                //I18n.getString("messages.connectionDialog.hibernateConnectionTestSuccessful",
                                "iReport successfully created a Hibernate session factory from your Spring configuration.",
                                "",JOptionPane.INFORMATION_MESSAGE);
                    }
            } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(Misc.getMainWindow(),e.getMessage(),
                            "Error",JOptionPane.ERROR_MESSAGE);

            }
        }
}
