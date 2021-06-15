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

import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrIntTestEntity;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.entities.reventity.trackmodifiedentities.CustomTrackingRevisionEntity;
import org.hibernate.envers.test.entities.reventity.trackmodifiedentities.CustomTrackingRevisionListener;
import org.hibernate.envers.test.entities.reventity.trackmodifiedentities.ModifiedEntityTypeEntity;
import org.hibernate.envers.test.tools.TestTools;


/**
 * Tests proper behavior of entity listener that implements {@link EntityTrackingRevisionListener}
 * interface. {@link CustomTrackingRevisionListener} shall be notified whenever an entity instance has been
 * added, modified or removed, so that changed entity name can be persisted.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class CustomTrackingEntitiesTest extends BaseEnversJPAFunctionalTestCase {
    private Integer steId = null;
    private Integer siteId = null;
    
    @Override
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(ModifiedEntityTypeEntity.class);
        cfg.addAnnotatedClass(StrTestEntity.class);
        cfg.addAnnotatedClass(StrIntTestEntity.class);
        cfg.addAnnotatedClass(CustomTrackingRevisionEntity.class);
    }

    @Test
    @Priority(10)
    public void initData() {
        EntityManager em = getEntityManager();

        // Revision 1 - Adding two entities
        em.getTransaction().begin();
        StrTestEntity ste = new StrTestEntity("x");
        StrIntTestEntity site = new StrIntTestEntity("y", 1);
        em.persist(ste);
        em.persist(site);
        steId = ste.getId();
        siteId = site.getId();
        em.getTransaction().commit();

        // Revision 2 - Modifying one entity
        em.getTransaction().begin();
        site = em.find(StrIntTestEntity.class, siteId);
        site.setNumber(2);
        em.getTransaction().commit();

        // Revision 3 - Deleting both entities
        em.getTransaction().begin();
        ste = em.find(StrTestEntity.class, steId);
        site = em.find(StrIntTestEntity.class, siteId);
        em.remove(ste);
        em.remove(site);
        em.getTransaction().commit();
    }

    @Test
    public void testTrackAddedEntities() {
        ModifiedEntityTypeEntity steDescriptor = new ModifiedEntityTypeEntity(StrTestEntity.class.getName());
        ModifiedEntityTypeEntity siteDescriptor = new ModifiedEntityTypeEntity(StrIntTestEntity.class.getName());

        CustomTrackingRevisionEntity ctre = getAuditReader().findRevision(CustomTrackingRevisionEntity.class, 1);

        assert ctre.getModifiedEntityTypes() != null;
        assert ctre.getModifiedEntityTypes().size() == 2;
        assert TestTools.makeSet(steDescriptor, siteDescriptor).equals(ctre.getModifiedEntityTypes());
    }

    @Test
    public void testTrackModifiedEntities() {
        ModifiedEntityTypeEntity siteDescriptor = new ModifiedEntityTypeEntity(StrIntTestEntity.class.getName());

        CustomTrackingRevisionEntity ctre = getAuditReader().findRevision(CustomTrackingRevisionEntity.class, 2);

        assert ctre.getModifiedEntityTypes() != null;
        assert ctre.getModifiedEntityTypes().size() == 1;
        assert TestTools.makeSet(siteDescriptor).equals(ctre.getModifiedEntityTypes());
    }

    @Test
    public void testTrackDeletedEntities() {
        ModifiedEntityTypeEntity steDescriptor = new ModifiedEntityTypeEntity(StrTestEntity.class.getName());
        ModifiedEntityTypeEntity siteDescriptor = new ModifiedEntityTypeEntity(StrIntTestEntity.class.getName());

        CustomTrackingRevisionEntity ctre = getAuditReader().findRevision(CustomTrackingRevisionEntity.class, 3);

        assert ctre.getModifiedEntityTypes() != null;
        assert ctre.getModifiedEntityTypes().size() == 2;
        assert TestTools.makeSet(steDescriptor, siteDescriptor).equals(ctre.getModifiedEntityTypes());
    }

    @Test(expected = AuditException.class)
    public void testFindEntitiesChangedInRevisionException() {
        getAuditReader().getCrossTypeRevisionChangesReader();
    }
}
