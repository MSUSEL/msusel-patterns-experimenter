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
import org.hibernate.internal.jaxb.mapping.orm.JaxbEmbeddable;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEntityListeners;
import org.hibernate.internal.jaxb.mapping.orm.JaxbIdClass;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPostLoad;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPostPersist;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPostRemove;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPostUpdate;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPrePersist;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPreRemove;
import org.hibernate.internal.jaxb.mapping.orm.JaxbPreUpdate;

/**
 * Mock <embeddable> to {@link javax.persistence.Embeddable @Embeddable}
 *
 * @author Strong Liu
 */
class EmbeddableMocker extends AbstractEntityObjectMocker {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			EmbeddableMocker.class.getName()
	);
	private JaxbEmbeddable embeddable;

	EmbeddableMocker(IndexBuilder indexBuilder, JaxbEmbeddable embeddable, EntityMappingsMocker.Default defaults) {
		super( indexBuilder, defaults );
		this.embeddable = embeddable;
	}

	@Override
	protected AbstractAttributesBuilder getAttributesBuilder() {
		if ( attributesBuilder == null ) {
			attributesBuilder = new EmbeddableAttributesBuilder(
					indexBuilder, classInfo, getAccessType(), getDefaults(), embeddable.getAttributes()
			);
		}
		return attributesBuilder;
	}

	@Override
	protected void processExtra() {
		create( EMBEDDABLE );
	}

	@Override
	protected void applyDefaults() {
		DefaultConfigurationHelper.INSTANCE.applyDefaults( embeddable, getDefaults() );
	}

	@Override
	protected boolean isMetadataComplete() {
		return embeddable.isMetadataComplete() != null && embeddable.isMetadataComplete();
	}

	@Override
	protected boolean isExcludeDefaultListeners() {
		return false;
	}

	@Override
	protected boolean isExcludeSuperclassListeners() {
		return false;
	}

	@Override
	protected JaxbIdClass getIdClass() {
		return null;
	}

	@Override
	protected JaxbEntityListeners getEntityListeners() {
		return null;
	}

	@Override
	protected JaxbAccessType getAccessType() {
		return embeddable.getAccess();
	}

	@Override
	protected String getClassName() {
		return embeddable.getClazz();
	}

	@Override
	protected JaxbPrePersist getPrePersist() {
		return null;
	}

	@Override
	protected JaxbPreRemove getPreRemove() {
		return null;
	}

	@Override
	protected JaxbPreUpdate getPreUpdate() {
		return null;
	}

	@Override
	protected JaxbPostPersist getPostPersist() {
		return null;
	}

	@Override
	protected JaxbPostUpdate getPostUpdate() {
		return null;
	}

	@Override
	protected JaxbPostRemove getPostRemove() {
		return null;
	}

	@Override
	protected JaxbPostLoad getPostLoad() {
		return null;
	}

	@Override
	protected JaxbAttributes getAttributes() {
		return null;
	}
}
