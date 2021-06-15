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
package org.hibernate.envers.test.integration.jta;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.IntTestEntity;
import org.hibernate.testing.jta.TestingJtaBootstrap;
import org.hibernate.testing.jta.TestingJtaPlatformImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Same as {@link org.hibernate.envers.test.integration.basic.Simple}, but in a JTA environment.
 * @author Adam Warski (adam at warski dot org)
 */
public class JtaTransaction extends BaseEnversJPAFunctionalTestCase  {
    private Integer id1;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{IntTestEntity.class};
    }

    @Override
    protected void addConfigOptions(Map options) {
        TestingJtaBootstrap.prepare(options);
    }

    @Test
    @Priority(10)
    public void initData() throws Exception {
		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();

        EntityManager em;
        IntTestEntity ite;
        try {
            em = getEntityManager();
            ite = new IntTestEntity(10);
            em.persist(ite);
            id1 = ite.getId();
        } finally {
			TestingJtaPlatformImpl.tryCommit();
        }
        em.close();

		TestingJtaPlatformImpl.INSTANCE.getTransactionManager().begin();

        try {
            em = getEntityManager();
            ite = em.find(IntTestEntity.class, id1);
            ite.setNumber(20);
        } finally {
			TestingJtaPlatformImpl.tryCommit();
        }
        em.close();
    }

	@Test
	public void testRevisionsCounts() throws Exception {
		Assert.assertEquals( 2, getAuditReader().getRevisions( 
				IntTestEntity.class, id1 ).size() );
	}

	@Test
	public void testHistoryOfId1() {
		IntTestEntity ver1 = new IntTestEntity( 10, id1 );
		IntTestEntity ver2 = new IntTestEntity( 20, id1 );

		List<Number> revisions = getAuditReader().getRevisions( 
				IntTestEntity.class, id1 );

		Assert.assertEquals( ver1, getAuditReader().find( 
				IntTestEntity.class, id1, revisions.get( 0 ) ) );
		Assert.assertEquals( ver2, getAuditReader().find( 
				IntTestEntity.class, id1, revisions.get( 1 ) ) );
	}
}
