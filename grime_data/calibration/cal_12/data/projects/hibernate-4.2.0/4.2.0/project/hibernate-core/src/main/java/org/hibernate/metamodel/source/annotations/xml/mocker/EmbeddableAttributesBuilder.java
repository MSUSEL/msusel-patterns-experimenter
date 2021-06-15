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

import java.util.Collections;
import java.util.List;

import org.jboss.jandex.ClassInfo;

import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbBasic;
import org.hibernate.internal.jaxb.mapping.orm.JaxbElementCollection;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEmbeddableAttributes;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEmbedded;
import org.hibernate.internal.jaxb.mapping.orm.JaxbEmbeddedId;
import org.hibernate.internal.jaxb.mapping.orm.JaxbId;
import org.hibernate.internal.jaxb.mapping.orm.JaxbManyToMany;
import org.hibernate.internal.jaxb.mapping.orm.JaxbManyToOne;
import org.hibernate.internal.jaxb.mapping.orm.JaxbOneToMany;
import org.hibernate.internal.jaxb.mapping.orm.JaxbOneToOne;
import org.hibernate.internal.jaxb.mapping.orm.JaxbTransient;
import org.hibernate.internal.jaxb.mapping.orm.JaxbVersion;

/**
 * @author Strong Liu
 */
class EmbeddableAttributesBuilder extends AbstractAttributesBuilder {
	private JaxbEmbeddableAttributes attributes;

	EmbeddableAttributesBuilder(IndexBuilder indexBuilder, ClassInfo classInfo, JaxbAccessType accessType, EntityMappingsMocker.Default defaults, JaxbEmbeddableAttributes embeddableAttributes) {
		super( indexBuilder, classInfo, defaults );
		this.attributes = embeddableAttributes;
	}

	@Override
	List<JaxbBasic> getBasic() {
		return attributes.getBasic();
	}

	@Override
	List<JaxbId> getId() {
		return Collections.emptyList();
	}

	@Override
	List<JaxbTransient> getTransient() {
		return attributes.getTransient();
	}

	@Override
	List<JaxbVersion> getVersion() {
		return Collections.emptyList();
	}

	@Override
	List<JaxbElementCollection> getElementCollection() {
		return attributes.getElementCollection();
	}

	@Override
	List<JaxbEmbedded> getEmbedded() {
		return attributes.getEmbedded();
	}

	@Override
	List<JaxbManyToMany> getManyToMany() {
		return attributes.getManyToMany();
	}

	@Override
	List<JaxbManyToOne> getManyToOne() {
		return attributes.getManyToOne();
	}

	@Override
	List<JaxbOneToMany> getOneToMany() {
		return attributes.getOneToMany();
	}

	@Override
	List<JaxbOneToOne> getOneToOne() {
		return attributes.getOneToOne();
	}

	@Override
	JaxbEmbeddedId getEmbeddedId() {
		return null;
	}
}
