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
package org.hibernate.envers.test.integration.collection.norevision;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.test.BaseEnversFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.junit.Test;

import java.util.List;

public abstract class AbstractCollectionChangeTest extends BaseEnversFunctionalTestCase  {
    protected Integer personId;

    @Override
    protected void configure(Configuration configuration) {
        configuration.setProperty("org.hibernate.envers.revision_on_collection_change", getCollectionChangeValue());
    }

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[]{Person.class, Name.class};
    }

    protected abstract String getCollectionChangeValue();

    protected abstract List<Integer> getExpectedPersonRevisions();

    @Test
    @Priority(10)
    public void initData() {
    	Session session = openSession();

        // Rev 1
        session.getTransaction().begin();        
        Person p = new Person();
        Name n = new Name();
        n.setName("name1");
        p.getNames().add(n);
        session.saveOrUpdate(p);
        session.getTransaction().commit();

        // Rev 2
        session.getTransaction().begin();
        n.setName("Changed name");
        session.saveOrUpdate(p);
        session.getTransaction().commit();

        // Rev 3
        session.getTransaction().begin();
        Name n2 = new Name();
        n2.setName("name2");
        p.getNames().add(n2);
        session.getTransaction().commit();

        personId = p.getId();
    }

    @Test
    public void testPersonRevisionCount() {
        assert getAuditReader().getRevisions(Person.class, personId).equals(getExpectedPersonRevisions());
    }
}
