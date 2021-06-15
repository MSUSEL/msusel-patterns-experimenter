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
package org.hibernate.envers.test.integration.readwriteexpression;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;

import org.hibernate.dialect.Oracle8iDialect;
import org.junit.Assert;
import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;

public class ReadWriteExpressionChange extends BaseEnversJPAFunctionalTestCase {

    private static final Double HEIGHT_INCHES = 73.0d;
    private static final Double HEIGHT_CENTIMETERS = HEIGHT_INCHES * 2.54d;

    private Integer id;

    @Override
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(Staff.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Staff staff = new Staff(HEIGHT_INCHES, 1);
        em.persist(staff);
        em.getTransaction().commit();
        id = staff.getId();
    }

    @Test
    public void shouldRespectWriteExpression() {
        EntityManager em = getEntityManager();
        List resultList = em.createNativeQuery("select size_in_cm from t_staff_AUD where id ="+id).getResultList();
        Assert.assertEquals(1, resultList.size());
        Double sizeInCm = null;
        if (getDialect() instanceof Oracle8iDialect) {
            sizeInCm = ((BigDecimal) resultList.get(0)).doubleValue();
        } else {
            sizeInCm = (Double) resultList.get(0);
        }
        Assert.assertEquals(HEIGHT_CENTIMETERS, sizeInCm.doubleValue(), 0.00000001);
    }

    @Test
    public void shouldRespectReadExpression() {
        List<Number> revisions = getAuditReader().getRevisions(Staff.class, id);
        Assert.assertEquals(1, revisions.size());
        Number number = revisions.get(0);
        Staff staffRev = getAuditReader().find(Staff.class, id, number);
        Assert.assertEquals(HEIGHT_INCHES, staffRev.getSizeInInches(), 0.00000001);
    }

}
