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
package org.hibernate.envers.test.integration.basic;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.test.BaseEnversFunctionalTestCase;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.integration.collection.norevision.Name;
import org.hibernate.envers.test.integration.collection.norevision.Person;
import org.hibernate.testing.TestForIssue;
import org.junit.Test;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@TestForIssue(jiraKey = "HHH-5565")
public class OutsideTransactionTest extends BaseEnversFunctionalTestCase {
    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{StrTestEntity.class, Person.class, Name.class};
    }

    @Override
    protected void configure(Configuration configuration) {
        configuration.setProperty("org.hibernate.envers.store_data_at_delete", "true");
        configuration.setProperty("org.hibernate.envers.revision_on_collection_change", "true");
    }

    @Test(expected = AuditException.class)
    public void testInsertOutsideActiveTransaction() {
        Session session = openSession();

        // Illegal insertion of entity outside of active transaction.
        StrTestEntity entity = new StrTestEntity("data");
        session.persist(entity);
        session.flush();

        session.close();
    }

    @Test(expected = AuditException.class)
    public void testUpdateOutsideActiveTransaction() {
        Session session = openSession();

        // Revision 1
        session.getTransaction().begin();
        StrTestEntity entity = new StrTestEntity("data");
        session.persist(entity);
        session.getTransaction().commit();

        // Illegal modification of entity state outside of active transaction.
        entity.setStr("modified data");
        session.update(entity);
        session.flush();

        session.close();
    }

    @Test(expected = AuditException.class)
    public void testDeleteOutsideActiveTransaction() {
        Session session = openSession();

        // Revision 1
        session.getTransaction().begin();
        StrTestEntity entity = new StrTestEntity("data");
        session.persist(entity);
        session.getTransaction().commit();

        // Illegal removal of entity outside of active transaction.
        session.delete(entity);
        session.flush();

        session.close();
    }

    @Test(expected = AuditException.class)
    public void testCollectionUpdateOutsideActiveTransaction() {
        Session session = openSession();

        // Revision 1
        session.getTransaction().begin();
        Person person = new Person();
        Name name = new Name();
        name.setName("Name");
        person.getNames().add(name);
        session.saveOrUpdate(person);
        session.getTransaction().commit();

        // Illegal collection update outside of active transaction.
        person.getNames().remove(name);
        session.saveOrUpdate(person);
        session.flush();

        session.close();
    }

    @Test(expected = AuditException.class)
    public void testCollectionRemovalOutsideActiveTransaction() {
        Session session = openSession();

        // Revision 1
        session.getTransaction().begin();
        Person person = new Person();
        Name name = new Name();
        name.setName("Name");
        person.getNames().add(name);
        session.saveOrUpdate(person);
        session.getTransaction().commit();

        // Illegal collection removal outside of active transaction.
        person.setNames(null);
        session.saveOrUpdate(person);
        session.flush();

        session.close();
    }
}
