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

import java.util.List;

import org.jboss.jandex.ClassInfo;

import org.hibernate.internal.jaxb.mapping.orm.JaxbBasic;
import org.hibernate.internal.jaxb.mapping.orm.JaxbElementCollection;
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
 * Abstract Parser to handle {@link org.hibernate.internal.jaxb.mapping.orm.JaxbAttributes JaxbAttributes}
 * and {@link org.hibernate.internal.jaxb.mapping.orm.JaxbEmbeddableAttributes JaxbEmbeddableAttributes}.
 *
 * It would be really helpful if these two classes can implement an interface with those abstract methods in this class.
 *
 * @author Strong Liu
 */
abstract class AbstractAttributesBuilder {

	private ClassInfo classInfo;
	private EntityMappingsMocker.Default defaults;
	private IndexBuilder indexBuilder;

	AbstractAttributesBuilder(IndexBuilder indexBuilder, ClassInfo classInfo, EntityMappingsMocker.Default defaults) {
		this.indexBuilder = indexBuilder;
		this.classInfo = classInfo;
		this.defaults = defaults;
	}

	final void parser() {
		for ( JaxbId id : getId() ) {
			new IdMocker( indexBuilder, classInfo, defaults, id ).process();
		}
		for ( JaxbTransient transientObj : getTransient() ) {
			new TransientMocker( indexBuilder, classInfo, defaults, transientObj ).process();
		}
		for ( JaxbVersion version : getVersion() ) {
			new VersionMocker( indexBuilder, classInfo, defaults, version ).process();
		}

		for ( JaxbBasic basic : getBasic() ) {
			new BasicMocker( indexBuilder, classInfo, defaults, basic ).process();
		}
		for ( JaxbElementCollection elementCollection : getElementCollection() ) {
			new ElementCollectionMocker(
					indexBuilder, classInfo, defaults, elementCollection
			).process();
		}
		for ( JaxbEmbedded embedded : getEmbedded() ) {
			new EmbeddedMocker( indexBuilder, classInfo, defaults, embedded ).process();
		}
		for ( JaxbManyToMany manyToMany : getManyToMany() ) {
			new ManyToManyMocker( indexBuilder, classInfo, defaults, manyToMany ).process();
		}

		for ( JaxbManyToOne manyToOne : getManyToOne() ) {
			new ManyToOneMocker( indexBuilder, classInfo, defaults, manyToOne ).process();
		}
		for ( JaxbOneToMany oneToMany : getOneToMany() ) {
			new OneToManyMocker(
					indexBuilder, classInfo, defaults, oneToMany
			).process();
		}
		for ( JaxbOneToOne oneToOne : getOneToOne() ) {
			new OneToOneMocker( indexBuilder, classInfo, defaults, oneToOne ).process();
		}
		if ( getEmbeddedId() != null ) {
			new EmbeddedIdMocker(
					indexBuilder, classInfo, defaults, getEmbeddedId()
			).process();
		}
	}

	abstract List<JaxbId> getId();

	abstract List<JaxbTransient> getTransient();

	abstract List<JaxbVersion> getVersion();

	abstract List<JaxbBasic> getBasic();

	abstract List<JaxbElementCollection> getElementCollection();

	abstract List<JaxbEmbedded> getEmbedded();

	abstract List<JaxbManyToMany> getManyToMany();

	abstract List<JaxbManyToOne> getManyToOne();

	abstract List<JaxbOneToMany> getOneToMany();

	abstract List<JaxbOneToOne> getOneToOne();

	abstract JaxbEmbeddedId getEmbeddedId();
}