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
package org.hibernate.envers.entities.mapper.relation;

import java.io.Serializable;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.mapper.relation.lazy.ToOneDelegateSessionImplementor;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.persister.entity.EntityPersister;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class ToOneEntityLoader {
	/**
	 * Immediately loads historical entity or its current state when excluded from audit process.
	 */
	public static Object loadImmediate(AuditReaderImplementor versionsReader, Class<?> entityClass, String entityName,
									   Object entityId, Number revision, AuditConfiguration verCfg) {
		if ( verCfg.getEntCfg().getNotVersionEntityConfiguration( entityName ) == null ) {
			// Audited relation, look up entity with Envers.
			return versionsReader.find( entityClass, entityName, entityId, revision );
		}
		else {
			// Not audited relation, look up entity with Hibernate.
			return versionsReader.getSessionImplementor().immediateLoad( entityName, (Serializable) entityId );
		}
	}

	/**
	 * Creates proxy of referenced *-to-one entity.
	 */
	public static Object createProxy(AuditReaderImplementor versionsReader, Class<?> entityClass, String entityName,
									 Object entityId, Number revision, AuditConfiguration verCfg) {
		EntityPersister persister = versionsReader.getSessionImplementor().getFactory().getEntityPersister( entityName );
		return persister.createProxy(
				(Serializable) entityId,
				new ToOneDelegateSessionImplementor( versionsReader, entityClass, entityId, revision, verCfg )
		);
	}

	/**
	 * Creates Hibernate proxy or retrieves the complete object of an entity if proxy is not
	 * allowed (e.g. @Proxy(lazy=false), final class).
	 */
	public static Object createProxyOrLoadImmediate(AuditReaderImplementor versionsReader, Class<?> entityClass, String entityName,
													Object entityId, Number revision, AuditConfiguration verCfg) {
		EntityPersister persister = versionsReader.getSessionImplementor().getFactory().getEntityPersister( entityName );
		if ( persister.hasProxy() ) {
			return createProxy( versionsReader, entityClass, entityName, entityId, revision, verCfg );
		}
		return loadImmediate( versionsReader, entityClass, entityName, entityId, revision, verCfg );
	}
}
