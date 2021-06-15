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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;

import org.junit.Test;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.envers.CrossTypeRevisionChangesReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;
import org.hibernate.envers.test.Priority;
import org.hibernate.envers.test.entities.StrIntTestEntity;
import org.hibernate.envers.test.entities.StrTestEntity;
import org.hibernate.envers.test.tools.TestTools;
import org.hibernate.envers.tools.Pair;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;

/**
 * Tests proper behavior of tracking modified entity names when {@code org.hibernate.envers.track_entities_changed_in_revision}
 * parameter is set to {@code true}.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@SuppressWarnings({"unchecked"})
public class DefaultTrackingEntitiesTest extends BaseEnversJPAFunctionalTestCase {
    private Integer steId = null;
    private Integer siteId = null;

    @Override
    public void configure(Ejb3Configuration cfg) {
        cfg.addAnnotatedClass(StrTestEntity.class);
        cfg.addAnnotatedClass(StrIntTestEntity.class);
    }

	@Override
	public void addConfigOptions(Map configuration) {
		super.addConfigOptions( configuration );
		configuration.put("org.hibernate.envers.track_entities_changed_in_revision", "true");
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
    public void testRevEntityTableCreation() {
        Iterator<Table> tableIterator = getCfg().getTableMappings();
        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            if ("REVCHANGES".equals(table.getName())) {
                assert table.getColumnSpan() == 2;
                assert table.getColumn(new Column("REV")) != null;
                assert table.getColumn(new Column("ENTITYNAME")) != null;
                return;
            }
        }
        assert false;
    }

    @Test
    public void testTrackAddedEntities() {
        StrTestEntity ste = new StrTestEntity("x", steId);
        StrIntTestEntity site = new StrIntTestEntity("y", 1, siteId);

        assert TestTools.checkList(getCrossTypeRevisionChangesReader().findEntities(1), ste, site);
    }

    @Test
    public void testTrackModifiedEntities() {
        StrIntTestEntity site = new StrIntTestEntity("y", 2, siteId);

        assert TestTools.checkList(getCrossTypeRevisionChangesReader().findEntities(2), site);
    }

    @Test
    public void testTrackDeletedEntities() {
        StrTestEntity ste = new StrTestEntity(null, steId);
        StrIntTestEntity site = new StrIntTestEntity(null, null, siteId);

        assert TestTools.checkList(getCrossTypeRevisionChangesReader().findEntities(3), site, ste);
    }

    @Test
    public void testFindChangesInInvalidRevision() {
        assert getCrossTypeRevisionChangesReader().findEntities(4).isEmpty();
    }

    @Test
    public void testTrackAddedEntitiesGroupByRevisionType() {
        StrTestEntity ste = new StrTestEntity("x", steId);
        StrIntTestEntity site = new StrIntTestEntity("y", 1, siteId);

        Map<RevisionType, List<Object>> result = getCrossTypeRevisionChangesReader().findEntitiesGroupByRevisionType(1);
        assert TestTools.checkList(result.get(RevisionType.ADD), site, ste);
        assert TestTools.checkList(result.get(RevisionType.MOD));
        assert TestTools.checkList(result.get(RevisionType.DEL));
    }

    @Test
    public void testTrackModifiedEntitiesGroupByRevisionType() {
        StrIntTestEntity site = new StrIntTestEntity("y", 2, siteId);

        Map<RevisionType, List<Object>> result = getCrossTypeRevisionChangesReader().findEntitiesGroupByRevisionType(2);
        assert TestTools.checkList(result.get(RevisionType.ADD));
        assert TestTools.checkList(result.get(RevisionType.MOD), site);
        assert TestTools.checkList(result.get(RevisionType.DEL));
    }

    @Test
    public void testTrackDeletedEntitiesGroupByRevisionType() {
        StrTestEntity ste = new StrTestEntity(null, steId);
        StrIntTestEntity site = new StrIntTestEntity(null, null, siteId);

        Map<RevisionType, List<Object>> result = getCrossTypeRevisionChangesReader().findEntitiesGroupByRevisionType(3);
        assert TestTools.checkList(result.get(RevisionType.ADD));
        assert TestTools.checkList(result.get(RevisionType.MOD));
        assert TestTools.checkList(result.get(RevisionType.DEL), site, ste);
    }

    @Test
    public void testFindChangedEntitiesByRevisionTypeADD() {
        StrTestEntity ste = new StrTestEntity("x", steId);
        StrIntTestEntity site = new StrIntTestEntity("y", 1, siteId);

        assert TestTools.checkList(getCrossTypeRevisionChangesReader().findEntities(1, RevisionType.ADD), ste, site);
    }

    @Test
    public void testFindChangedEntitiesByRevisionTypeMOD() {
        StrIntTestEntity site = new StrIntTestEntity("y", 2, siteId);

        assert TestTools.checkList(getCrossTypeRevisionChangesReader().findEntities(2, RevisionType.MOD), site);
    }

    @Test
    public void testFindChangedEntitiesByRevisionTypeDEL() {
        StrTestEntity ste = new StrTestEntity(null, steId);
        StrIntTestEntity site = new StrIntTestEntity(null, null, siteId);

        assert TestTools.checkList(getCrossTypeRevisionChangesReader().findEntities(3, RevisionType.DEL), ste, site);
    }

    @Test
    public void testFindEntityTypesChangedInRevision() {
        assert TestTools.makeSet(Pair.make(StrTestEntity.class.getName(), StrTestEntity.class),
                                 Pair.make(StrIntTestEntity.class.getName(), StrIntTestEntity.class))
                        .equals(getCrossTypeRevisionChangesReader().findEntityTypes(1));

        assert TestTools.makeSet(Pair.make(StrIntTestEntity.class.getName(), StrIntTestEntity.class))
                        .equals(getCrossTypeRevisionChangesReader().findEntityTypes(2));
        
        assert TestTools.makeSet(Pair.make(StrTestEntity.class.getName(), StrTestEntity.class),
                                 Pair.make(StrIntTestEntity.class.getName(), StrIntTestEntity.class))
                        .equals(getCrossTypeRevisionChangesReader().findEntityTypes(3));
    }

    private CrossTypeRevisionChangesReader getCrossTypeRevisionChangesReader() {
        return getAuditReader().getCrossTypeRevisionChangesReader();
    }
}
