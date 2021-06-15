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
import javax.persistence.EntityManager;

import org.junit.Ignore;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.entities.StrTestEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Ignore
public class InsertsOneTransactionPerformance extends AbstractPerformanceTest {
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrTestEntity.class);
    }

    private final static int NUMBER_INSERTS = 5000;

    protected void doTest() {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        start();
        for (int i=0; i<NUMBER_INSERTS; i++) {
            entityManager.persist(new StrTestEntity("x" + i));
        }
        entityManager.getTransaction().commit();
        stop();
    }

    public static void main(String[] args) throws IOException {
        InsertsOneTransactionPerformance insertsOneTransactionPerformance = new InsertsOneTransactionPerformance();
        insertsOneTransactionPerformance.test(3);
    }
}