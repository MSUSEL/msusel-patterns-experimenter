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
//$Id$
package org.hibernate.ejb.test.ejb3configuration;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.test.Cat;
import org.hibernate.ejb.test.Kitten;
import org.hibernate.internal.util.ConfigHelper;
import org.hibernate.testing.RequiresDialect;

/**
 * @author Emmanuel Bernard
 */
@RequiresDialect(H2Dialect.class)
public class ProgrammaticConfTest {
    @Test
	public void testProgrammaticAPI() throws Exception {
		Ejb3Configuration conf = new Ejb3Configuration();
		conf.addAnnotatedClass( Cat.class );
		conf.addAnnotatedClass( Kitten.class );
		conf.addProperties(getProperties());
		EntityManagerFactory emf = conf.buildEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		Cat cat = new Cat();
		cat.setAge( 23 );
		cat.setDateOfBirth( new Date() );
		cat.setLength( 32 );
		cat.setName( "Tomy" );
		em.getTransaction().begin();
		em.persist( cat );
		em.flush();
		Assert.assertNotNull( em.find( Cat.class, cat.getId() ) );
		em.getTransaction().rollback();
		emf.close();
	}
    @Test
	public void testProgrammaticCfg() throws Exception {
		Ejb3Configuration conf = new Ejb3Configuration();
		conf.configure( "org/hibernate/ejb/test/ejb3configuration/hibernate.cfg.xml" );
		conf.addProperties(getProperties());
		EntityManagerFactory emf = conf.buildEntityManagerFactory();
		EntityManager em = emf.createEntityManager();
		Cat cat = new Cat();
		cat.setAge( 23 );
		cat.setDateOfBirth( new Date() );
		cat.setLength( 32 );
		cat.setName( "Tomy" );
		em.getTransaction().begin();
		em.persist( cat );
		em.flush();
		Assert.assertNotNull( em.find(Cat.class, cat.getId() ) );
		em.getTransaction().rollback();
		emf.close();
	}

	protected Properties getProperties() throws IOException {
		Properties properties = new Properties( );
		InputStream stream = ConfigHelper.getResourceAsStream("/hibernate.properties");
		try {
			properties.load(stream);
		}

		finally {
			try{
				stream.close();
			}
			catch (IOException ioe){
			}
		}
		properties.setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
		return properties;
	}


}
