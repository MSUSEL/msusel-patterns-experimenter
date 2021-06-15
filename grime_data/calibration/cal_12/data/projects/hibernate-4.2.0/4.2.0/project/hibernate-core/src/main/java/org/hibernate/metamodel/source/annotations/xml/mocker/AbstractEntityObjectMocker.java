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

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import org.hibernate.AssertionFailure;
import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.internal.jaxb.mapping.orm.JaxbAttributes;
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
 * @author Strong Liu
 */
abstract class AbstractEntityObjectMocker extends AnnotationMocker {
	private ListenerMocker listenerParser;
	protected AbstractAttributesBuilder attributesBuilder;
	protected ClassInfo classInfo;

	AbstractEntityObjectMocker(IndexBuilder indexBuilder, EntityMappingsMocker.Default defaults) {
		super( indexBuilder, defaults );
	}

	private boolean isPreProcessCalled = false;

	/**
	 * Pre-process Entity Objects to find the default {@link javax.persistence.Access} for later attributes processing.
	 */
	final void preProcess() {
		applyDefaults();
		classInfo = indexBuilder.createClassInfo( getClassName() );
		DotName classDotName = classInfo.name();
		if ( isMetadataComplete() ) {
			indexBuilder.metadataComplete( classDotName );
		}
		parserAccessType( getAccessType(), getTarget() );
		isPreProcessCalled = true;
	}

	final void process() {
		if ( !isPreProcessCalled ) {
			throw new AssertionFailure( "preProcess should be called before process" );
		}
		if ( getAccessType() == null ) {
			JaxbAccessType accessType = AccessHelper.getEntityAccess( getTargetName(), indexBuilder );
			if ( accessType == null ) {
				accessType = getDefaults().getAccess();
			}
			parserAccessType( accessType, getTarget() );
		}
		processExtra();
		if ( isExcludeDefaultListeners() ) {
			create( EXCLUDE_DEFAULT_LISTENERS );
		}
		if ( isExcludeSuperclassListeners() ) {
			create( EXCLUDE_SUPERCLASS_LISTENERS );
		}
		parserIdClass( getIdClass() );

		if ( getAttributes() != null ) {
			getAttributesBuilder().parser();

		}
		if ( getEntityListeners() != null ) {
			getListenerParser().parser( getEntityListeners() );
		}
		getListenerParser().parser( getPrePersist() );
		getListenerParser().parser( getPreRemove() );
		getListenerParser().parser( getPreUpdate() );
		getListenerParser().parser( getPostPersist() );
		getListenerParser().parser( getPostUpdate() );
		getListenerParser().parser( getPostRemove() );
		getListenerParser().parser( getPostLoad() );

		indexBuilder.finishEntityObject( getTargetName(), getDefaults() );
	}


	abstract protected void processExtra();

	/**
	 * give a chance to the sub-classes to override defaults configuration
	 */
	abstract protected void applyDefaults();

	abstract protected boolean isMetadataComplete();

	abstract protected boolean isExcludeDefaultListeners();

	abstract protected boolean isExcludeSuperclassListeners();

	abstract protected JaxbIdClass getIdClass();

	abstract protected JaxbEntityListeners getEntityListeners();

	abstract protected JaxbAccessType getAccessType();

	abstract protected String getClassName();

	abstract protected JaxbPrePersist getPrePersist();

	abstract protected JaxbPreRemove getPreRemove();

	abstract protected JaxbPreUpdate getPreUpdate();

	abstract protected JaxbPostPersist getPostPersist();

	abstract protected JaxbPostUpdate getPostUpdate();

	abstract protected JaxbPostRemove getPostRemove();

	abstract protected JaxbPostLoad getPostLoad();

	abstract protected JaxbAttributes getAttributes();

	protected ListenerMocker getListenerParser() {
		if ( listenerParser == null ) {
			listenerParser = new ListenerMocker( indexBuilder, classInfo );
		}
		return listenerParser;
	}

	protected AbstractAttributesBuilder getAttributesBuilder() {
		if ( attributesBuilder == null ) {
			attributesBuilder = new AttributesBuilder(
					indexBuilder, classInfo, getAccessType(), getDefaults(), getAttributes()
			);
		}
		return attributesBuilder;
	}

	protected AnnotationInstance parserIdClass(JaxbIdClass idClass) {
		if ( idClass == null ) {
			return null;
		}
		String className = MockHelper.buildSafeClassName( idClass.getClazz(), getDefaults().getPackageName() );
		return create(
				ID_CLASS, MockHelper.classValueArray(
				"value", className, indexBuilder.getServiceRegistry()
		)
		);
	}


	@Override
	protected DotName getTargetName() {
		return classInfo.name();
	}

	@Override
	protected AnnotationTarget getTarget() {
		return classInfo;
	}
}
