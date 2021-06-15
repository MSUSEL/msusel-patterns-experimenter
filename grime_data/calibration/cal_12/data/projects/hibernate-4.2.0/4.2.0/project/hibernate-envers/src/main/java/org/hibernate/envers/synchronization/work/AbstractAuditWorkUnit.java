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
package org.hibernate.envers.synchronization.work;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.configuration.AuditEntitiesConfiguration;
import org.hibernate.envers.strategy.AuditStrategy;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Stephanie Pau at Markit Group Plc
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public abstract class AbstractAuditWorkUnit implements AuditWorkUnit {
	protected final SessionImplementor sessionImplementor;
    protected final AuditConfiguration verCfg;
    protected final Serializable id;
    protected final String entityName;
    protected final AuditStrategy auditStrategy;
    protected final RevisionType revisionType;

    private Object performedData;

    protected AbstractAuditWorkUnit(SessionImplementor sessionImplementor, String entityName, AuditConfiguration verCfg,
									Serializable id, RevisionType revisionType) {
		this.sessionImplementor = sessionImplementor;
        this.verCfg = verCfg;
        this.id = id;
        this.entityName = entityName;
        this.revisionType = revisionType;
        this.auditStrategy = verCfg.getAuditStrategy();
    }

    protected void fillDataWithId(Map<String, Object> data, Object revision) {
        AuditEntitiesConfiguration entitiesCfg = verCfg.getAuditEntCfg();

        Map<String, Object> originalId = new HashMap<String, Object>();
        originalId.put(entitiesCfg.getRevisionFieldName(), revision);

        verCfg.getEntCfg().get(getEntityName()).getIdMapper().mapToMapFromId(originalId, id);
        data.put(entitiesCfg.getRevisionTypePropName(), revisionType);
        data.put(entitiesCfg.getOriginalIdPropName(), originalId);
    }

    public void perform(Session session, Object revisionData) {
        Map<String, Object> data = generateData(revisionData);

        auditStrategy.perform(session, getEntityName(), verCfg, id, data, revisionData);

        setPerformed(data);
    }

    public Serializable getEntityId() {
        return id;
    }

    public boolean isPerformed() {
        return performedData != null;
    }

    public String getEntityName() {
        return entityName;
    }

    protected void setPerformed(Object performedData) {
        this.performedData = performedData;
    }

    public void undo(Session session) {
        if (isPerformed()) {
            session.delete(verCfg.getAuditEntCfg().getAuditEntityName(getEntityName()), performedData);
            session.flush();
        }
    }

    public RevisionType getRevisionType() {
        return revisionType;
    }
}
