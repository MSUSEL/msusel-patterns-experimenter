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
import java.util.Date;
import java.util.HashSet;
import javax.persistence.EntityManager;

import org.junit.Ignore;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.performance.complex.ChildEntity1;
import org.hibernate.envers.test.performance.complex.ChildEntity2;
import org.hibernate.envers.test.performance.complex.RootEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Ignore
public class ComplexInsertPerformance extends AbstractPerformanceTest {
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(RootEntity.class);
        cfg.addAnnotatedClass(ChildEntity1.class);
        cfg.addAnnotatedClass(ChildEntity2.class);
    }

    private final static int NUMBER_INSERTS = 1000;

    private long idCounter = 0;

    private ChildEntity2 createChildEntity2() {
        ChildEntity2 ce = new ChildEntity2();
        ce.setId(idCounter++);
        ce.setNumber(12345678);
        ce.setData("some data, not really meaningful");
        ce.setStrings(new HashSet<String>());
        ce.getStrings().add("aaa");
        ce.getStrings().add("bbb");
        ce.getStrings().add("ccc");

        return ce;
    }

    private ChildEntity1 createChildEntity1() {
        ChildEntity1 ce = new ChildEntity1();
        ce.setId(idCounter++);
        ce.setData1("xxx");
        ce.setData2("yyy");
        ce.setChild1(createChildEntity2());
        ce.setChild2(createChildEntity2());

        return ce;
    }

    protected void doTest() {
        for (int i=0; i<NUMBER_INSERTS; i++) {
            newEntityManager();
            EntityManager entityManager = getEntityManager();

            entityManager.getTransaction().begin();

            RootEntity re = new RootEntity();
            re.setId(idCounter++);
            re.setData1("data1");
            re.setData2("data2");
            re.setDate1(new Date());
            re.setNumber1(123);
            re.setNumber2(456);
            re.setChild1(createChildEntity1());
            re.setChild2(createChildEntity1());
            re.setChild3(createChildEntity1());

            start();
            entityManager.persist(re);            
            entityManager.getTransaction().commit();
            stop();
        }
    }

    public static void main(String[] args) throws IOException {
        ComplexInsertPerformance insertsPerformance = new ComplexInsertPerformance();
        insertsPerformance.test(3);
    }
}
