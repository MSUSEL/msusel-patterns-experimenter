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
package org.hibernate.envers.test.integration.modifiedflags;

import java.util.List;
import java.util.Map;

import org.hibernate.envers.configuration.GlobalConfiguration;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.test.BaseEnversJPAFunctionalTestCase;

/**
 * Base test for modified flags feature
 * 
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public abstract class AbstractModifiedFlagsEntityTest extends BaseEnversJPAFunctionalTestCase {
	@Override
	protected void addConfigOptions(Map options) {
		super.addConfigOptions(options);
		if (forceModifiedFlags()) {
			options.put(GlobalConfiguration.GLOBAL_WITH_MODIFIED_FLAG_PROPERTY, "true");
		}
	}

	public boolean forceModifiedFlags() {
		return true;
	}

	protected List queryForPropertyHasChanged(Class<?> clazz, Object id,
											  String... propertyNames) {
		AuditQuery query = createForRevisionsQuery(clazz, id, false);
		addHasChangedProperties(query, propertyNames);
		return query.getResultList();
	}

	protected List queryForPropertyHasChangedWithDeleted(Class<?> clazz, Object id,
											  String... propertyNames) {
		AuditQuery query = createForRevisionsQuery(clazz, id, true);
		addHasChangedProperties(query, propertyNames);
		return query.getResultList();
	}

	protected List queryForPropertyHasNotChanged(Class<?> clazz, Object id,
												 String... propertyNames) {
		AuditQuery query = createForRevisionsQuery(clazz, id, false);
		addHasNotChangedProperties(query, propertyNames);
		return query.getResultList();
	}

	protected List queryForPropertyHasNotChangedWithDeleted(Class<?> clazz, Object id,
												 String... propertyNames) {
		AuditQuery query = createForRevisionsQuery(clazz, id, true);
		addHasNotChangedProperties(query, propertyNames);
		return query.getResultList();
	}

	private void addHasChangedProperties(AuditQuery query,
										 String[] propertyNames) {
		for (String propertyName : propertyNames) {
			query.add(AuditEntity.property(propertyName).hasChanged());
		}
	}

	private void addHasNotChangedProperties(AuditQuery query,
											String[] propertyNames) {
		for (String propertyName : propertyNames) {
			query.add(AuditEntity.property(propertyName).hasNotChanged());
		}
	}

	private AuditQuery createForRevisionsQuery(Class<?> clazz, Object id, boolean withDeleted) {
		return getAuditReader().createQuery()
				.forRevisionsOfEntity(clazz, false, withDeleted)
				.add(AuditEntity.id().eq(id));
	}

}
