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

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.tools.Tools;
import org.hibernate.persister.entity.EntityPersister;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class AddWorkUnit extends AbstractAuditWorkUnit implements AuditWorkUnit {
    private final Object[] state;
    private final Map<String, Object> data;

    public AddWorkUnit(SessionImplementor sessionImplementor, String entityName, AuditConfiguration verCfg,
					   Serializable id, EntityPersister entityPersister, Object[] state) {
        super(sessionImplementor, entityName, verCfg, id, RevisionType.ADD);

        this.data = new HashMap<String, Object>();
        this.state = state;
        this.verCfg.getEntCfg().get(getEntityName()).getPropertyMapper().map(sessionImplementor, data,
				entityPersister.getPropertyNames(), state, null);
    }

    public AddWorkUnit(SessionImplementor sessionImplementor, String entityName, AuditConfiguration verCfg,
                       Serializable id, Map<String, Object> data) {
        super(sessionImplementor, entityName, verCfg, id, RevisionType.ADD);

        this.data = data;
        final String[] propertyNames = sessionImplementor.getFactory().getEntityPersister(getEntityName()).getPropertyNames();
        this.state = Tools.mapToArray(data, propertyNames);
    }

    public boolean containsWork() {
        return true;
    }

    public Map<String, Object> generateData(Object revisionData) {
        fillDataWithId(data, revisionData);
        return data;
    }

    public Object[] getState() {
        return state;
    }

    public AuditWorkUnit merge(AddWorkUnit second) {
        return second;
    }

    public AuditWorkUnit merge(ModWorkUnit second) {
        return new AddWorkUnit(sessionImplementor, entityName, verCfg, id, second.getData());
    }

    public AuditWorkUnit merge(DelWorkUnit second) {
        return null;
    }

    public AuditWorkUnit merge(CollectionChangeWorkUnit second) {
		second.mergeCollectionModifiedData(data);
		return this;
	}

    public AuditWorkUnit merge(FakeBidirectionalRelationWorkUnit second) {
        return FakeBidirectionalRelationWorkUnit.merge(second, this, second.getNestedWorkUnit());
    }

    public AuditWorkUnit dispatch(WorkUnitMergeVisitor first) {
        return first.merge(this);
    }
}
