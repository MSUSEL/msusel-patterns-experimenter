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
package org.hibernate.envers.test.integration.onetomany.embeddedid;

import javax.persistence.EntityManager;

import org.hibernate.envers.test.Priority;
import org.hibernate.testing.TestForIssue;
import org.junit.Assert;
import org.junit.Test;

import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;

import java.util.Arrays;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-7157")
public class MapsIdTest extends BaseEnversJPAFunctionalTestCase {
    private PersonTuple tuple1Ver1 = null;
    private PersonTuple tuple2Ver1 = null;
    private PersonTuple tuple2Ver2 = null;
    private Person personCVer1 = null;
    private Person personCVer2 = null;

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{Person.class, PersonTuple.class, Constant.class};
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1
        em.getTransaction().begin();
        Person personA = new Person("Peter");
        Person personB = new Person("Mary");
        em.persist(personA);
        em.persist(personB);
        Constant cons = new Constant("USD", "US Dollar");
        em.persist(cons);
        PersonTuple tuple1 = new PersonTuple(true, personA, personB, cons);
        em.persist(tuple1);
        em.getTransaction().commit();

        tuple1Ver1 = new PersonTuple(tuple1.isHelloWorld(), tuple1.getPersonA(), tuple1.getPersonB(), tuple1.getConstant());

        // Revision 2
        em.getTransaction().begin();
        cons = em.find(Constant.class, cons.getId());
        Person personC1 = new Person("Lukasz");
        em.persist(personC1);
        PersonTuple tuple2 = new PersonTuple(true, personA, personC1, cons);
        em.persist(tuple2);
        em.getTransaction().commit();

        tuple2Ver1 = new PersonTuple(tuple2.isHelloWorld(), tuple2.getPersonA(), tuple2.getPersonB(), tuple2.getConstant());
        personCVer1 = new Person(personC1.getId(), personC1.getName());
        personCVer1.getPersonBTuples().add(tuple2Ver1);

        // Revision 3
        em.getTransaction().begin();
        tuple2 = em.find(PersonTuple.class, tuple2.getPersonTupleId());
        tuple2.setHelloWorld(false);
        em.merge(tuple2);
        em.getTransaction().commit();

        tuple2Ver2 = new PersonTuple(tuple2.isHelloWorld(), tuple2.getPersonA(), tuple2.getPersonB(), tuple2.getConstant());

        // Revision 4
        em.getTransaction().begin();
        Person personC2 = em.find(Person.class, personC1.getId());
        personC2.setName("Robert");
        em.merge(personC2);
        em.getTransaction().commit();

        personCVer2 = new Person(personC2.getId(), personC2.getName());
        personCVer2.getPersonBTuples().add(tuple2Ver1);

        em.close();
    }

    @Test
    public void testRevisionsCounts() {
        Assert.assertEquals(Arrays.asList(1), getAuditReader().getRevisions(PersonTuple.class, tuple1Ver1.getPersonTupleId()));
        Assert.assertEquals(Arrays.asList(2, 3), getAuditReader().getRevisions(PersonTuple.class, tuple2Ver1.getPersonTupleId()));
        Assert.assertEquals(Arrays.asList(2, 4), getAuditReader().getRevisions(Person.class, personCVer1.getId()));
    }

    @Test
    public void testHistoryOfTuple1() {
        PersonTuple tuple = getAuditReader().find(PersonTuple.class, tuple1Ver1.getPersonTupleId(), 1);

        Assert.assertEquals(tuple1Ver1, tuple);
        Assert.assertEquals(tuple1Ver1.isHelloWorld(), tuple.isHelloWorld());
        Assert.assertEquals(tuple1Ver1.getPersonA().getId(), tuple.getPersonA().getId());
        Assert.assertEquals(tuple1Ver1.getPersonB().getId(), tuple.getPersonB().getId());
    }

    @Test
    public void testHistoryOfTuple2() {
        PersonTuple tuple = getAuditReader().find(PersonTuple.class, tuple2Ver2.getPersonTupleId(), 2);

        Assert.assertEquals(tuple2Ver1, tuple);
        Assert.assertEquals(tuple2Ver1.isHelloWorld(), tuple.isHelloWorld());
        Assert.assertEquals(tuple2Ver1.getPersonA().getId(), tuple.getPersonA().getId());
        Assert.assertEquals(tuple2Ver1.getPersonB().getId(), tuple.getPersonB().getId());

        tuple = getAuditReader().find(PersonTuple.class, tuple2Ver2.getPersonTupleId(), 3);

        Assert.assertEquals(tuple2Ver2, tuple);
        Assert.assertEquals(tuple2Ver2.isHelloWorld(), tuple.isHelloWorld());
        Assert.assertEquals(tuple2Ver2.getPersonA().getId(), tuple.getPersonA().getId());
        Assert.assertEquals(tuple2Ver2.getPersonB().getId(), tuple.getPersonB().getId());
    }

    @Test
    public void testHistoryOfPersonC() {
        Person person = getAuditReader().find(Person.class, personCVer1.getId(), 2);

        Assert.assertEquals(personCVer1, person);
        Assert.assertEquals(personCVer1.getPersonATuples(), person.getPersonATuples());
        Assert.assertEquals(personCVer1.getPersonBTuples(), person.getPersonBTuples());

        person = getAuditReader().find(Person.class, personCVer2.getId(), 4);

        Assert.assertEquals(personCVer2, person);
    }
}