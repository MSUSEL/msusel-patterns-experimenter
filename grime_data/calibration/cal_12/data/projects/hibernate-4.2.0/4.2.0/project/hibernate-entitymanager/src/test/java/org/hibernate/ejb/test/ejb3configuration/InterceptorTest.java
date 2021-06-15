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
package org.hibernate.ejb.test.ejb3configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.test.Distributor;
import org.hibernate.ejb.test.Item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Emmanuel Bernard
 */
public class InterceptorTest {
    @Test
    public void testInjectedInterceptor() {
        EntityManagerFactory emf = constructConfiguration().setInterceptor( new ExceptionInterceptor() )
                .createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Item i = new Item();
        i.setName( "Laptop" );
        try {
            em.getTransaction().begin();
            em.persist( i );
            em.getTransaction().commit();
        }
        catch ( IllegalStateException e ) {
            assertEquals( ExceptionInterceptor.EXCEPTION_MESSAGE, e.getMessage() );
        }
        finally {
            if ( em.getTransaction() != null && em.getTransaction().isActive() ) {
                em.getTransaction().rollback();
            }
            em.close();
            emf.close();
        }
    }

    @Test
    public void testConfiguredInterceptor() {
        EntityManagerFactory emf = constructConfiguration().setProperty(
                AvailableSettings.INTERCEPTOR,
                ExceptionInterceptor.class.getName()
        ).createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Item i = new Item();
        i.setName( "Laptop" );
        try {
            em.getTransaction().begin();
            em.persist( i );
            em.getTransaction().commit();
            fail( "No interceptor" );
        }
        catch ( IllegalStateException e ) {
            assertEquals( ExceptionInterceptor.EXCEPTION_MESSAGE, e.getMessage() );
        }
        finally {
            if ( em.getTransaction() != null && em.getTransaction().isActive() ) {
                em.getTransaction().rollback();
            }
            em.close();
            emf.close();
        }
    }

    @Test
    public void testConfiguredSessionInterceptor() {
        EntityManagerFactory emf = constructConfiguration().setProperty(
                AvailableSettings.SESSION_INTERCEPTOR,
                LocalExceptionInterceptor.class.getName()
        ).setProperty( "aaaaaaaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbb" ).createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Item i = new Item();
        i.setName( "Laptop" );
        try {
            em.getTransaction().begin();
            em.persist( i );
            em.getTransaction().commit();
            fail( "No interceptor" );
        }
        catch ( IllegalStateException e ) {
            assertEquals( LocalExceptionInterceptor.LOCAL_EXCEPTION_MESSAGE, e.getMessage() );
        }
        finally {
            if ( em.getTransaction() != null && em.getTransaction().isActive() ) {
                em.getTransaction().rollback();
            }
            em.close();
            emf.close();
        }
    }

    @Test
    public void testEmptyCreateEntityManagerFactoryAndPropertyUse() {
        EntityManagerFactory emf = constructConfiguration().setProperty(
                AvailableSettings.INTERCEPTOR,
                ExceptionInterceptor.class.getName()
        ).createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Item i = new Item();
        i.setName( "Laptop" );
        try {
            em.getTransaction().begin();
            em.persist( i );
            em.getTransaction().commit();
            fail( "No interceptor" );
        }
        catch ( IllegalStateException e ) {
            assertEquals( ExceptionInterceptor.EXCEPTION_MESSAGE, e.getMessage() );
        }
        finally {
            if ( em.getTransaction() != null && em.getTransaction().isActive() ) {
                em.getTransaction().rollback();
            }
            em.close();
            emf.close();
        }
    }

    @Test
    public void testOnLoadCallInInterceptor() {
        EntityManagerFactory emf = constructConfiguration().setInterceptor( new ExceptionInterceptor( true ) )
                .createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Item i = new Item();
        i.setName( "Laptop" );
        em.getTransaction().begin();
        em.persist( i );
        em.flush();
        em.clear();
        try {
            em.find( Item.class, i.getName() );
            fail( "No interceptor" );
        }
        catch ( IllegalStateException e ) {
            assertEquals( ExceptionInterceptor.EXCEPTION_MESSAGE, e.getMessage() );
        }
        finally {
            if ( em.getTransaction() != null && em.getTransaction().isActive() ) {
                em.getTransaction().rollback();
            }
            em.close();
            emf.close();
        }
    }


    protected Ejb3Configuration constructConfiguration() {
        Ejb3Configuration ejb3Configuration = new Ejb3Configuration();
        ejb3Configuration.getHibernateConfiguration().setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
        ejb3Configuration
                .getHibernateConfiguration()
                .setProperty( org.hibernate.cfg.AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "true" );
        ejb3Configuration
                .getHibernateConfiguration()
                .setProperty( Environment.DIALECT, Dialect.getDialect().getClass().getName() );
        for ( Class clazz : getAnnotatedClasses() ) {
            ejb3Configuration.addAnnotatedClass( clazz );
        }
        return ejb3Configuration;
    }

    public Class[] getAnnotatedClasses() {
        return new Class[] {
                Item.class,
                Distributor.class
        };
    }

}
