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
package org.hibernate.metamodel.source.annotations.xml.mocker;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbAttributes;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEntityListeners;
import org.hibernate.internal.jaxb.mapping.orm.JaxbIdClass;
import org.hibernate.internal.jaxb.mapping.orm.JaxbMappedSuperclass;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPostLoad;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPostPersist;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPostRemove;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPostUpdate;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPrePersist;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPreRemove;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPreUpdate;

/**
 * Mock <mapped-superclass> to {@link javax.persistence.MappedSuperclass @MappedSuperClass}
 *
 * @author Strong Liu
 */
class MappedSuperclassMocker extends AbstractEntityObjectMocker {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			MappedSuperclassMocker.class.getName()
	);
	private JaxbMappedSuperclass mappedSuperclass;

	MappedSuperclassMocker(IndexBuilder indexBuilder, JaxbMappedSuperclass mappedSuperclass, EntityMappingsMocker.Default defaults) {
		super( indexBuilder, defaults );
		this.mappedSuperclass = mappedSuperclass;
	}

	@Override
	protected void applyDefaults() {
		DefaultConfigurationHelper.INSTANCE.applyDefaults( mappedSuperclass, getDefaults() );
	}

	@Override
	protected void processExtra() {
		create( MAPPED_SUPERCLASS );
	}

	@Override
	protected JaxbAttributes getAttributes() {
		return mappedSuperclass.getAttributes();
	}

	@Override
	protected JaxbAccessType getAccessType() {
		return mappedSuperclass.getAccess();
	}

	@Override
	protected boolean isMetadataComplete() {
		return mappedSuperclass.isMetadataComplete() != null && mappedSuperclass.isMetadataComplete();
	}

	@Override
	protected boolean isExcludeDefaultListeners() {
		return mappedSuperclass.getExcludeDefaultListeners() != null;
	}

	@Override
	protected boolean isExcludeSuperclassListeners() {
		return mappedSuperclass.getExcludeSuperclassListeners() != null;
	}

	@Override
	protected JaxbIdClass getIdClass() {
		return mappedSuperclass.getIdClass();
	}

	@Override
	protected JaxbEntityListeners getEntityListeners() {
		return mappedSuperclass.getEntityListeners();
	}

	protected String getClassName() {
		return mappedSuperclass.getClazz();
	}

	@Override
	protected JaxbPrePersist getPrePersist() {
		return mappedSuperclass.getPrePersist();
	}

	@Override
	protected JaxbPreRemove getPreRemove() {
		return mappedSuperclass.getPreRemove();
	}

	@Override
	protected JaxbPreUpdate getPreUpdate() {
		return mappedSuperclass.getPreUpdate();
	}

	@Override
	protected JaxbPostPersist getPostPersist() {
		return mappedSuperclass.getPostPersist();
	}

	@Override
	protected JaxbPostUpdate getPostUpdate() {
		return mappedSuperclass.getPostUpdate();
	}

	@Override
	protected JaxbPostRemove getPostRemove() {
		return mappedSuperclass.getPostRemove();
	}

	@Override
	protected JaxbPostLoad getPostLoad() {
		return mappedSuperclass.getPostLoad();
	}
}
