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

import org.hibernate.cfg.Configuration;
import org.hibernate.envers.test.BaseEnversFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.integration.entityNames.manyToManyAudited.Car;
import org.hibernate.envers.test.integration.entityNames.manyToManyAudited.Person;
import org.hibernate.envers.test.tools.TestTools;
import org.hibernate.envers.tools.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class EntityNamesTest extends BaseEnversFunctionalTestCase {
    @Override
    protected String[] getMappings() {
        return new String[]{"mappings/entityNames/manyToManyAudited/mappings.hbm.xml"};
    }

    @Override
    protected void configure(Configuration configuration) {
        configuration.setProperty("org.hibernate.envers.track_entities_changed_in_revision", "true");
    }

    @Test
    @Priority(10)
    public void initData() {
        Person pers1 = new Person("Hernan", 28);
        Person pers2 = new Person("Leandro", 29);
        Person pers3 = new Person("Barba", 32);
        Person pers4 = new Person("Camomo", 15);

        // Revision 1
        getSession().getTransaction().begin();
        List<Person > owners = new ArrayList<Person>();
        owners.add(pers1);
        owners.add(pers2);
        owners.add(pers3);
        Car car1 = new Car(5, owners);
        getSession().persist(car1);
        getSession().getTransaction().commit();
        long person1Id = pers1.getId();

        // Revision 2
        owners = new ArrayList<Person>();
        owners.add(pers2);
        owners.add(pers3);
        owners.add(pers4);
        Car car2 = new Car(27, owners);
        getSession().getTransaction().begin();
        Person person1 = (Person)getSession().get("Personaje", person1Id);
        person1.setName("Hernan David");
        person1.setAge(40);
        getSession().persist(car1);
        getSession().persist(car2);
        getSession().getTransaction().commit();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testModifiedEntityTypes() {
        assert TestTools.makeSet(Pair.make(Car.class.getName(), Car.class),
                                 Pair.make("Personaje", Person.class))
                        .equals(getAuditReader().getCrossTypeRevisionChangesReader().findEntityTypes(1));
        assert TestTools.makeSet(Pair.make(Car.class.getName(), Car.class),
                                 Pair.make("Personaje", Person.class))
                        .equals(getAuditReader().getCrossTypeRevisionChangesReader().findEntityTypes(2));
    }
}