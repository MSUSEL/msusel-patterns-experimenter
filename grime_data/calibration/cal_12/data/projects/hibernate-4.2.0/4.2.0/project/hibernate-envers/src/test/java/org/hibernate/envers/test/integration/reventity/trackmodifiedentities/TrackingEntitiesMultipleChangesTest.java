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
package org.hibernate.envers.test.integration.reventity.trackmodifiedentities;

import java.util.HashSet;
import java.util.Map;
import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.CrossTypeRevisionChangesReader;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.tools.TestTools;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class TrackingEntitiesMultipleChangesTest extends BaseEnversJPAFunctionalTestCase {
    private Integer steId1 = null;
    private Integer steId2 = null;

    @Override
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrTestEntity.class);
    }

	@Override
	protected void addConfigOptions(Map options) {
		super.addConfigOptions( options );
		options.put( "org.hibernate.envers.track_entities_changed_in_revision", "true" ) ;
	}

	@Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1 - Adding two entities
        em.getTransaction().begin();
        StrTestEntity ste1 = new StrTestEntity("x");
        StrTestEntity ste2 = new StrTestEntity("y");
        em.persist(ste1);
        em.persist(ste2);
        steId1 = ste1.getId();
        steId2 = ste2.getId();
        em.getTransaction().commit();

        // Revision 2 - Adding first and removing second entity
        em.getTransaction().begin();
        ste1 = em.find(StrTestEntity.class, steId1);
        ste2 = em.find(StrTestEntity.class, steId2);
        ste1.setStr("z");
        em.remove(ste2);
        em.getTransaction().commit();

        // Revision 3 - Modifying and removing the same entity.
        em.getTransaction().begin();
        ste1 = em.find(StrTestEntity.class, steId1);
        ste1.setStr("a");
        em.merge(ste1);
        em.remove(ste1);
        em.getTransaction().commit();
    }

    @Test
    public void testTrackAddedTwoEntities() {
        StrTestEntity ste1 = new StrTestEntity("x", steId1);
        StrTestEntity ste2 = new StrTestEntity("y", steId2);

        Assert.assertEquals(TestTools.makeSet(ste1, ste2), new HashSet<Object>(getCrossTypeRevisionChangesReader().findEntities(1)));
    }

    @Test
    public void testTrackUpdateAndRemoveDifferentEntities() {
        StrTestEntity ste1 = new StrTestEntity("z", steId1);
        StrTestEntity ste2 = new StrTestEntity(null, steId2);

        Assert.assertEquals(TestTools.makeSet(ste1, ste2), new HashSet<Object>(getCrossTypeRevisionChangesReader().findEntities(2)));
    }

    @Test
    public void testTrackUpdateAndRemoveTheSameEntity() {
        StrTestEntity ste1 = new StrTestEntity(null, steId1);

        Assert.assertEquals(TestTools.makeSet(ste1), new HashSet<Object>(getCrossTypeRevisionChangesReader().findEntities(3)));
    }

    private CrossTypeRevisionChangesReader getCrossTypeRevisionChangesReader() {
        return getAuditReader().getCrossTypeRevisionChangesReader();
    }
}