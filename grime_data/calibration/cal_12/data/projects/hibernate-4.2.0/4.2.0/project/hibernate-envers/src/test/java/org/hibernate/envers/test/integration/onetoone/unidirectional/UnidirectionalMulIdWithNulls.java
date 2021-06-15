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
package org.hibernate.envers.test.integration.onetoone.unidirectional;

import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.ids.EmbId;
import org.hibernate.envers.test.entities.ids.EmbIdTestEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class UnidirectionalMulIdWithNulls extends BaseEnversJPAFunctionalTestCase {
    private EmbId ei;
    
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(EmbIdTestEntity.class);
        cfg.addAnnotatedClass(UniRefIngMulIdEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        ei = new EmbId(1, 2);

        EntityManager em = getEntityManager();
        
        // Revision 1
        EmbIdTestEntity eite = new EmbIdTestEntity(ei, "data");
        UniRefIngMulIdEntity notNullRef = new UniRefIngMulIdEntity(1, "data 1", eite);
        UniRefIngMulIdEntity nullRef = new UniRefIngMulIdEntity(2, "data 2", null);

        em.getTransaction().begin();
        em.persist(eite);
        em.persist(notNullRef);
        em.persist(nullRef);
        em.getTransaction().commit();
    }

    @Test
    public void testNullReference() {
        UniRefIngMulIdEntity nullRef = getAuditReader().find(UniRefIngMulIdEntity.class, 2, 1);
        assertNull(nullRef.getReference());
    }

    @Test
    public void testNotNullReference() {
        EmbIdTestEntity eite = getAuditReader().find(EmbIdTestEntity.class, ei, 1);
        UniRefIngMulIdEntity notNullRef = getAuditReader().find(UniRefIngMulIdEntity.class, 1, 1);
        assertNotNull(notNullRef.getReference());
        assertEquals(notNullRef.getReference(), eite);
    }
}