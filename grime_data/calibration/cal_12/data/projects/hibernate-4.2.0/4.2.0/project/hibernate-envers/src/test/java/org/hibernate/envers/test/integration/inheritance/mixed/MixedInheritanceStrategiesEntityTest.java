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
package org.hibernate.envers.test.integration.inheritance.mixed;

import java.util.Arrays;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase ;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.integration.inheritance.mixed.entities.AbstractActivity;
import org.hibernate.envers.test.integration.inheritance.mixed.entities.AbstractCheckActivity;
import org.hibernate.envers.test.integration.inheritance.mixed.entities.Activity;
import org.hibernate.envers.test.integration.inheritance.mixed.entities.ActivityId;
import org.hibernate.envers.test.integration.inheritance.mixed.entities.CheckInActivity;
import org.hibernate.envers.test.integration.inheritance.mixed.entities.NormalActivity;

import static org.junit.Assert.assertEquals;

/**
 * @author Michal Skowronek (mskowr at o2 pl)
 */
public class MixedInheritanceStrategiesEntityTest extends BaseEnversJPAFunctionalTestCase  {

	private ActivityId id2;
	private ActivityId id1;
	private ActivityId id3;

	@Override
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(AbstractActivity.class);
        cfg.addAnnotatedClass(AbstractCheckActivity.class);
        cfg.addAnnotatedClass(CheckInActivity.class);
        cfg.addAnnotatedClass(NormalActivity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        NormalActivity normalActivity = new NormalActivity();
		id1 = new ActivityId(1, 2);
		normalActivity.setId(id1);
        normalActivity.setSequenceNumber(1);

        // Revision 1
        getEntityManager().getTransaction().begin();

        getEntityManager().persist(normalActivity);

        getEntityManager().getTransaction().commit();
        // Revision 2
        getEntityManager().getTransaction().begin();

        normalActivity = getEntityManager().find(NormalActivity.class, id1);
        CheckInActivity checkInActivity = new CheckInActivity();
		id2 = new ActivityId(2, 3);
		checkInActivity.setId(id2);
        checkInActivity.setSequenceNumber(0);
        checkInActivity.setDurationInMinutes(30);
        checkInActivity.setRelatedActivity(normalActivity);

        getEntityManager().persist(checkInActivity);

        getEntityManager().getTransaction().commit();

        // Revision 3
        normalActivity = new NormalActivity();
		id3 = new ActivityId(3, 4);
		normalActivity.setId(id3);
        normalActivity.setSequenceNumber(2);

        getEntityManager().getTransaction().begin();

        getEntityManager().persist(normalActivity);

        getEntityManager().getTransaction().commit();

        // Revision 4
        getEntityManager().getTransaction().begin();

        normalActivity = getEntityManager().find(NormalActivity.class, id3);
        checkInActivity = getEntityManager().find(CheckInActivity.class, id2);
        checkInActivity.setRelatedActivity(normalActivity);

        getEntityManager().merge(checkInActivity);

        getEntityManager().getTransaction().commit();
    }

    @Test
    public void testRevisionsCounts() {
        assertEquals(Arrays.asList(1), getAuditReader().getRevisions(NormalActivity.class, id1));
        assertEquals(Arrays.asList(3), getAuditReader().getRevisions(NormalActivity.class, id3));
        assertEquals(Arrays.asList(2, 4), getAuditReader().getRevisions(CheckInActivity.class, id2));
    }

    @Test
    public void testCurrentStateOfCheckInActivity() {

        final CheckInActivity checkInActivity = getEntityManager().find(CheckInActivity.class, id2);
        final NormalActivity normalActivity = getEntityManager().find(NormalActivity.class, id3);

        assertEquals(id2, checkInActivity.getId());
        assertEquals(0, checkInActivity.getSequenceNumber().intValue());
        assertEquals(30, checkInActivity.getDurationInMinutes().intValue());
        final Activity relatedActivity = checkInActivity.getRelatedActivity();
        assertEquals(normalActivity.getId(), relatedActivity.getId());
        assertEquals(normalActivity.getSequenceNumber(), relatedActivity.getSequenceNumber());
    }

    @Test
    public void testCheckCurrentStateOfNormalActivities() throws Exception {
        final NormalActivity normalActivity1 = getEntityManager().find(NormalActivity.class, id1);
        final NormalActivity normalActivity2 = getEntityManager().find(NormalActivity.class, id3);

        assertEquals(id1, normalActivity1.getId());
        assertEquals(1, normalActivity1.getSequenceNumber().intValue());
        assertEquals(id3, normalActivity2.getId());
        assertEquals(2, normalActivity2.getSequenceNumber().intValue());
    }

    @Test
    public void doTestFirstRevisionOfCheckInActivity() throws Exception {
        CheckInActivity checkInActivity = getAuditReader().find(CheckInActivity.class, id2, 2);
        NormalActivity normalActivity = getAuditReader().find(NormalActivity.class, id1, 2);

        assertEquals(id2, checkInActivity.getId());
        assertEquals(0, checkInActivity.getSequenceNumber().intValue());
        assertEquals(30, checkInActivity.getDurationInMinutes().intValue());
        Activity relatedActivity = checkInActivity.getRelatedActivity();
        assertEquals(normalActivity.getId(), relatedActivity.getId());
        assertEquals(normalActivity.getSequenceNumber(), relatedActivity.getSequenceNumber());
    }

    @Test
    public void doTestSecondRevisionOfCheckInActivity() throws Exception {
        CheckInActivity checkInActivity = getAuditReader().find(CheckInActivity.class, id2, 4);
        NormalActivity normalActivity = getAuditReader().find(NormalActivity.class, id3, 4);

        assertEquals(id2, checkInActivity.getId());
        assertEquals(0, checkInActivity.getSequenceNumber().intValue());
        assertEquals(30, checkInActivity.getDurationInMinutes().intValue());
        Activity relatedActivity = checkInActivity.getRelatedActivity();
        assertEquals(normalActivity.getId(), relatedActivity.getId());
        assertEquals(normalActivity.getSequenceNumber(), relatedActivity.getSequenceNumber());
    }

}
