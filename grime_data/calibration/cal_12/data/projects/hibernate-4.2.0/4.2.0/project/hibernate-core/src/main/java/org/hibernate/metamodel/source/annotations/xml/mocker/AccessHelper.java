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
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.logging.Logger;

import org.hibernate.AssertionFailure;
import org.hibernate.MappingException;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.jaxb.mapping.orm.JaxbAccessType;
import org.hibernate.metamodel.source.annotations.JPADotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;
import org.hibernate.metamodel.source.annotations.xml.PseudoJpaDotNames;

/**
 * @author Strong Liu
 */
class AccessHelper implements JPADotNames {
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			AccessHelper.class.getName()
	);

	static JaxbAccessType getAccessFromDefault(IndexBuilder indexBuilder) {
		AnnotationInstance annotationInstance = JandexHelper.getSingleAnnotation(
				indexBuilder.getAnnotations(),
				PseudoJpaDotNames.DEFAULT_ACCESS
		);
		if ( annotationInstance == null ) {
			return null;
		}
		else {
			return JandexHelper.getEnumValue( annotationInstance, "value", JaxbAccessType.class );
		}

	}

	static JaxbAccessType getAccessFromIdPosition(DotName className, IndexBuilder indexBuilder) {
		Map<DotName, List<AnnotationInstance>> indexedAnnotations = indexBuilder.getIndexedAnnotations( className );
		Map<DotName, List<AnnotationInstance>> ormAnnotations = indexBuilder.getClassInfoAnnotationsMap( className );
		JaxbAccessType accessType = getAccessFromIdPosition( ormAnnotations );
		if ( accessType == null ) {
			accessType = getAccessFromIdPosition( indexedAnnotations );
		}
		if ( accessType == null ) {
			ClassInfo parent = indexBuilder.getClassInfo( className );
			if ( parent == null ) {
				parent = indexBuilder.getIndexedClassInfo( className );
			}
			if ( parent != null ) {
				DotName parentClassName = parent.superName();
				accessType = getAccessFromIdPosition( parentClassName, indexBuilder );
			}

		}

		return accessType;
	}

	private static JaxbAccessType getAccessFromIdPosition(Map<DotName, List<AnnotationInstance>> annotations) {
		if ( annotations == null || annotations.isEmpty() || !( annotations.containsKey( ID ) ) ) {
			return null;
		}
		List<AnnotationInstance> idAnnotationInstances = annotations.get( ID );
		if ( MockHelper.isNotEmpty( idAnnotationInstances ) ) {
			return processIdAnnotations( idAnnotationInstances );
		}
		return null;
	}

	private static JaxbAccessType processIdAnnotations(List<AnnotationInstance> idAnnotations) {
		JaxbAccessType accessType = null;
		for ( AnnotationInstance annotation : idAnnotations ) {
			AnnotationTarget tmpTarget = annotation.target();
			if ( tmpTarget == null ) {
				throw new AssertionFailure( "@Id has no AnnotationTarget, this is mostly a internal error." );
			}
			if ( accessType == null ) {
				accessType = annotationTargetToAccessType( tmpTarget );
			}
			else {
				if ( !accessType.equals( annotationTargetToAccessType( tmpTarget ) ) ) {
					throw new MappingException( "Inconsistent placement of @Id annotation within hierarchy " );
				}
			}
		}
		return accessType;
	}

	static JaxbAccessType annotationTargetToAccessType(AnnotationTarget target) {
		return ( target instanceof MethodInfo ) ? JaxbAccessType.PROPERTY : JaxbAccessType.FIELD;
	}

	static JaxbAccessType getEntityAccess(DotName className, IndexBuilder indexBuilder) {
		Map<DotName, List<AnnotationInstance>> indexedAnnotations = indexBuilder.getIndexedAnnotations( className );
		Map<DotName, List<AnnotationInstance>> ormAnnotations = indexBuilder.getClassInfoAnnotationsMap( className );
		JaxbAccessType accessType = getAccess( ormAnnotations );
		if ( accessType == null ) {
			accessType = getAccess( indexedAnnotations );
		}
		if ( accessType == null ) {
			ClassInfo parent = indexBuilder.getClassInfo( className );
			if ( parent == null ) {
				parent = indexBuilder.getIndexedClassInfo( className );
			}
			if ( parent != null ) {
				DotName parentClassName = parent.superName();
				accessType = getEntityAccess( parentClassName, indexBuilder );
			}
		}
		return accessType;

	}

	private static JaxbAccessType getAccess(Map<DotName, List<AnnotationInstance>> annotations) {
		if ( annotations == null || annotations.isEmpty() || !isEntityObject( annotations ) ) {
			return null;
		}
		List<AnnotationInstance> accessAnnotationInstances = annotations.get( JPADotNames.ACCESS );
		if ( MockHelper.isNotEmpty( accessAnnotationInstances ) ) {
			for ( AnnotationInstance annotationInstance : accessAnnotationInstances ) {
				if ( annotationInstance.target() != null && annotationInstance.target() instanceof ClassInfo ) {
					return JandexHelper.getEnumValue(
							annotationInstance,
							"value",
							JaxbAccessType.class
					);
				}
			}
		}
		return null;
	}

	private static boolean isEntityObject(Map<DotName, List<AnnotationInstance>> annotations) {
		return annotations.containsKey( ENTITY ) || annotations.containsKey( MAPPED_SUPERCLASS ) || annotations
				.containsKey( EMBEDDABLE );
	}

	/**
	 * Get {@link javax.persistence.AccessType } from {@link javax.persistence.Access @Access} on the attribute of the given class
	 */
	static JaxbAccessType getAccessFromAttributeAnnotation(DotName className, String attributeName, IndexBuilder indexBuilder) {
		Map<DotName, List<AnnotationInstance>> indexedAnnotations = indexBuilder.getIndexedAnnotations( className );
		if ( indexedAnnotations != null && indexedAnnotations.containsKey( ACCESS ) ) {
			List<AnnotationInstance> annotationInstances = indexedAnnotations.get( ACCESS );
			if ( MockHelper.isNotEmpty( annotationInstances ) ) {
				for ( AnnotationInstance annotationInstance : annotationInstances ) {
					AnnotationTarget indexedPropertyTarget = annotationInstance.target();
					if ( indexedPropertyTarget == null ) {
						continue;
					}
					if ( JandexHelper.getPropertyName( indexedPropertyTarget ).equals( attributeName ) ) {
						JaxbAccessType accessType = JandexHelper.getEnumValue(
								annotationInstance,
								"value",
								JaxbAccessType.class
						);
						/**
						 * here we ignore @Access(FIELD) on property (getter) and @Access(PROPERTY) on field
						 */
						JaxbAccessType targetAccessType = annotationTargetToAccessType( indexedPropertyTarget );
						if ( accessType.equals( targetAccessType ) ) {
							return targetAccessType;
						}
						else {
							LOG.warn(
									String.format(
											"%s.%s has @Access on %s, but it tries to assign the access type to %s, this is not allowed by JPA spec, and will be ignored.",
											className,
											attributeName,
											targetAccessType,
											accessType
									)
							);
						}
					}
				}
			}
		}
		return null;
	}
}
