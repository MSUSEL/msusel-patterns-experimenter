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
package org.hibernate.envers.test.integration.cache;

import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.onetomany.SetRefEdEntity;
import org.hibernate.envers.test.entities.onetomany.SetRefIngEntity;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@SuppressWarnings({"ObjectEquality"})
public class OneToManyCache extends BaseEnversJPAFunctionalTestCase {
    private Integer ed1_id;
    private Integer ed2_id;

    private Integer ing1_id;
    private Integer ing2_id;

    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(SetRefEdEntity.class);
        cfg.addAnnotatedClass(SetRefIngEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        SetRefEdEntity ed1 = new SetRefEdEntity(1, "data_ed_1");
        SetRefEdEntity ed2 = new SetRefEdEntity(2, "data_ed_2");

        SetRefIngEntity ing1 = new SetRefIngEntity(1, "data_ing_1");
        SetRefIngEntity ing2 = new SetRefIngEntity(2, "data_ing_2");

        // Revision 1
        em.getTransaction().begin();

        em.persist(ed1);
        em.persist(ed2);

        ing1.setReference(ed1);
        ing2.setReference(ed1);

        em.persist(ing1);
        em.persist(ing2);

        em.getTransaction().commit();

        // Revision 2
        em.getTransaction().begin();

        ing1 = em.find(SetRefIngEntity.class, ing1.getId());
        ing2 = em.find(SetRefIngEntity.class, ing2.getId());
        ed2 = em.find(SetRefEdEntity.class, ed2.getId());

        ing1.setReference(ed2);
        ing2.setReference(ed2);

        em.getTransaction().commit();

        //

        ed1_id = ed1.getId();
        ed2_id = ed2.getId();

        ing1_id = ing1.getId();
        ing2_id = ing2.getId();
    }

    @Test
    public void testCacheReferenceAccessAfterFind() {
        SetRefEdEntity ed1_rev1 = getAuditReader().find(SetRefEdEntity.class, ed1_id, 1);

        SetRefIngEntity ing1_rev1 = getAuditReader().find(SetRefIngEntity.class, ing1_id, 1);
        SetRefIngEntity ing2_rev1 = getAuditReader().find(SetRefIngEntity.class, ing2_id, 1);

        // It should be exactly the same object
        assert ing1_rev1.getReference() == ed1_rev1;
        assert ing2_rev1.getReference() == ed1_rev1;
    }

    @Test
    public void testCacheReferenceAccessAfterCollectionAccessRev1() {
        SetRefEdEntity ed1_rev1 = getAuditReader().find(SetRefEdEntity.class, ed1_id, 1);

        // It should be exactly the same object
        assert ed1_rev1.getReffering().size() == 2;
        for (SetRefIngEntity setRefIngEntity : ed1_rev1.getReffering()) {
            assert setRefIngEntity.getReference() == ed1_rev1;
        }
    }

    @Test
    public void testCacheReferenceAccessAfterCollectionAccessRev2() {
        SetRefEdEntity ed2_rev2 = getAuditReader().find(SetRefEdEntity.class, ed2_id, 2);

        assert ed2_rev2.getReffering().size() == 2;
        for (SetRefIngEntity setRefIngEntity : ed2_rev2.getReffering()) {
            assert setRefIngEntity.getReference() == ed2_rev2;
        }
    }

    @Test
    public void testCacheFindAfterCollectionAccessRev1() {
        SetRefEdEntity ed1_rev1 = getAuditReader().find(SetRefEdEntity.class, ed1_id, 1);

        // Reading the collection
        assert ed1_rev1.getReffering().size() == 2;

        SetRefIngEntity ing1_rev1 = getAuditReader().find(SetRefIngEntity.class, ing1_id, 1);
        SetRefIngEntity ing2_rev1 = getAuditReader().find(SetRefIngEntity.class, ing2_id, 1);

        for (SetRefIngEntity setRefIngEntity : ed1_rev1.getReffering()) {
            assert setRefIngEntity == ing1_rev1 || setRefIngEntity == ing2_rev1;
        }
    }
}