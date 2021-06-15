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
package org.hibernate.envers.entities.mapper.relation.lazy;
import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.EntitiesConfigurations;
import org.hibernate.envers.entities.mapper.relation.ToOneEntityLoader;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author Tomasz Bech
 * @author HernпїЅn Chanfreau
 */
public class ToOneDelegateSessionImplementor extends AbstractDelegateSessionImplementor {
	private static final long serialVersionUID = 4770438372940785488L;
	
    private final AuditReaderImplementor versionsReader;
    private final Class<?> entityClass;
    private final Object entityId;
    private final Number revision;
    private final AuditConfiguration verCfg;

	public ToOneDelegateSessionImplementor(AuditReaderImplementor versionsReader,
                                           Class<?> entityClass, Object entityId, Number revision,
                                           AuditConfiguration verCfg) {
        super(versionsReader.getSessionImplementor());
        this.versionsReader = versionsReader;
        this.entityClass = entityClass;
        this.entityId = entityId;
        this.revision = revision;
        this.verCfg = verCfg;
    }

    public Object doImmediateLoad(String entityName) throws HibernateException {
        return ToOneEntityLoader.loadImmediate( versionsReader, entityClass, entityName, entityId, revision, verCfg );
    }
}
