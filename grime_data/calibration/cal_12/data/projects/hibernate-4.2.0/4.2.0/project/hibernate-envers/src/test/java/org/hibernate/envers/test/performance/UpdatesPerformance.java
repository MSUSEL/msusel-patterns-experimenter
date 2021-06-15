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
package org.hibernate.envers.test.performance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;

import org.junit.Ignore;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.entities.StrTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Ignore
public class UpdatesPerformance extends AbstractPerformanceTest {
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrTestEntity.class);
    }

    private final static int NUMBER_UPDATES = 5000;
    private final static int NUMBER_ENTITIES = 10;

    private Random random = new Random();

    private List<Integer> ids = new ArrayList<Integer>();

    private void setup() {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();

        for (int i=0; i<NUMBER_ENTITIES; i++) {
            StrTestEntity testEntity = new StrTestEntity("x" + i);
            entityManager.persist(testEntity);
            ids.add(testEntity.getId());
        }
        entityManager.getTransaction().commit();
    }

    protected void doTest() {
        setup();

        for (int i=0; i<NUMBER_UPDATES; i++) {
            newEntityManager();
            EntityManager entityManager = getEntityManager();

            entityManager.getTransaction().begin();
            Integer id = ids.get(random.nextInt(NUMBER_ENTITIES));
            start();
            StrTestEntity testEntity = entityManager.find(StrTestEntity.class, id);
            testEntity.setStr("z" + i);
            entityManager.getTransaction().commit();
            stop();
        }
    }

    public static void main(String[] args) throws IOException {
        UpdatesPerformance updatesPerformance = new UpdatesPerformance();
        updatesPerformance.test(3);
    }
}
