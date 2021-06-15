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
package org.hibernate.test.annotations.xml.hbm;

import java.util.HashSet;

import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;

/**
 * @author Emmanuel Bernard
 */
public class HbmTest extends BaseCoreFunctionalTestCase {
	@Test
	public void testManyToOne() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		Government gov = new Government();
		gov.setName( "Liberals" );
		s.save( gov );
		PrimeMinister pm = new PrimeMinister();
		pm.setName( "Murray" );
		pm.setCurrentGovernment( gov );
		s.save( pm );
		s.getTransaction().rollback();
		s.close();
	}

	@Test
	public void testOneToMany() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		Government gov = new Government();
		gov.setName( "Liberals" );
		Government gov2 = new Government();
		gov2.setName( "Liberals2" );
		s.save( gov );
		s.save( gov2 );
		PrimeMinister pm = new PrimeMinister();
		pm.setName( "Murray" );
		pm.setCurrentGovernment( gov );
		pm.setGovernments( new HashSet() );
		pm.getGovernments().add( gov2 );
		pm.getGovernments().add( gov );
		gov.setPrimeMinister( pm );
		gov2.setPrimeMinister( pm );
		s.save( pm );
		s.flush();
		s.getTransaction().rollback();
		s.close();
	}

	@Test
	public void testManyToMany() throws Exception {
		Session s = openSession();
		s.getTransaction().begin();
		CloudType type = new CloudType();
		type.setName( "Cumulus" );
		Sky sky = new Sky();
		s.persist( type );
		sky.getCloudTypes().add(type);
		s.persist( sky );
		s.flush();
		s.getTransaction().rollback();
		s.close();
	}

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[]{
				PrimeMinister.class,
				Sky.class,
		};
	}

	@Override
	protected String[] getXmlFiles() {
		return new String[]{
				"org/hibernate/test/annotations/xml/hbm/Government.hbm.xml",
				"org/hibernate/test/annotations/xml/hbm/CloudType.hbm.xml",
		};
	}
}
