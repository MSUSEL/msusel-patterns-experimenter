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
package org.hibernate.ejb.test.xml;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.SharedCacheMode;

import junit.framework.Assert;
import org.junit.Test;

import org.hibernate.Session;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.test.BaseEntityManagerFunctionalTestCase;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;

/**
 * @author Emmanuel Bernard
 */
public class XmlTest extends BaseEntityManagerFunctionalTestCase {
	@Test
	public void testXmlMappingCorrectness() throws Exception {
		EntityManager em = getOrCreateEntityManager();
		em.close();
	}

	@Test
	public void testXmlMappingWithCacheable() throws Exception{
		EntityManager em = getOrCreateEntityManager();
		SessionImplementor session = em.unwrap( SessionImplementor.class );
		EntityPersister entityPersister= session.getFactory().getEntityPersister( Lighter.class.getName() );
		Assert.assertTrue(entityPersister.hasCache());
	}

	@Override
	public Class[] getAnnotatedClasses() {
		return new Class[0];
	}

	protected void addConfigOptions(Map options) {
		options.put(  AvailableSettings.SHARED_CACHE_MODE, SharedCacheMode.ENABLE_SELECTIVE );
	}

	@Override
	public String[] getEjb3DD() {
		return new String[] {
				"org/hibernate/ejb/test/xml/orm.xml",
				"org/hibernate/ejb/test/xml/orm2.xml",
		};
	}
}
