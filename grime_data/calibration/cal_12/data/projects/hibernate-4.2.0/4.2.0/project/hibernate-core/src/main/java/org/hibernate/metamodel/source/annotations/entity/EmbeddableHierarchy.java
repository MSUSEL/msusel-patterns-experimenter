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
package org.hibernate.metamodel.source.annotations.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.AccessType;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import org.hibernate.AssertionFailure;
import org.hibernate.metamodel.source.annotations.AnnotationBindingContext;
import org.hibernate.metamodel.source.annotations.JPADotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;

/**
 * Contains information about the access and inheritance type for all classes within a class hierarchy.
 *
 * @author Hardy Ferentschik
 */
public class EmbeddableHierarchy implements Iterable<EmbeddableClass> {
	private final AccessType defaultAccessType;
	private final List<EmbeddableClass> embeddables;

	/**
	 * Builds the configured class hierarchy for a an embeddable class.
	 *
	 * @param embeddableClass the top level embedded class
	 * @param propertyName the name of the property in the entity class embedding this embeddable
	 * @param accessType the access type inherited from the class in which the embeddable gets embedded
	 * @param context the annotation binding context with access to the service registry and the annotation index
	 *
	 * @return a set of {@code ConfiguredClassHierarchy}s. One for each "leaf" entity.
	 */
	public static EmbeddableHierarchy createEmbeddableHierarchy(Class<?> embeddableClass, String propertyName, AccessType accessType, AnnotationBindingContext context) {

		ClassInfo embeddableClassInfo = context.getClassInfo( embeddableClass.getName() );
		if ( embeddableClassInfo == null ) {
			throw new AssertionFailure(
					String.format(
							"The specified class %s cannot be found in the annotation index",
							embeddableClass.getName()
					)
			);
		}

		if ( JandexHelper.getSingleAnnotation( embeddableClassInfo, JPADotNames.EMBEDDABLE ) == null ) {
			throw new AssertionFailure(
					String.format(
							"The specified class %s is not annotated with @Embeddable even though it is as embeddable",
							embeddableClass.getName()
					)
			);
		}

		List<ClassInfo> classInfoList = new ArrayList<ClassInfo>();
		ClassInfo tmpClassInfo;
		Class<?> clazz = embeddableClass;
		while ( clazz != null && !clazz.equals( Object.class ) ) {
			tmpClassInfo = context.getIndex().getClassByName( DotName.createSimple( clazz.getName() ) );
			clazz = clazz.getSuperclass();
			if ( tmpClassInfo == null ) {
				continue;
			}

			classInfoList.add( 0, tmpClassInfo );
		}

		return new EmbeddableHierarchy(
				classInfoList,
				propertyName,
				context,
				accessType
		);
	}

	@SuppressWarnings("unchecked")
	private EmbeddableHierarchy(
			List<ClassInfo> classInfoList,
			String propertyName,
			AnnotationBindingContext context,
			AccessType defaultAccessType) {
		this.defaultAccessType = defaultAccessType;

		// the resolved type for the top level class in the hierarchy
		context.resolveAllTypes( classInfoList.get( classInfoList.size() - 1 ).name().toString() );

		embeddables = new ArrayList<EmbeddableClass>();
		ConfiguredClass parent = null;
		EmbeddableClass embeddable;
		for ( ClassInfo info : classInfoList ) {
			embeddable = new EmbeddableClass(
					info, propertyName, parent, defaultAccessType, context
			);
			embeddables.add( embeddable );
			parent = embeddable;
		}
	}


	public AccessType getDefaultAccessType() {
		return defaultAccessType;
	}

	/**
	 * @return An iterator iterating in top down manner over the configured classes in this hierarchy.
	 */
	public Iterator<EmbeddableClass> iterator() {
		return embeddables.iterator();
	}

	/**
	 * @return Returns the leaf configured class
	 */
	public EmbeddableClass getLeaf() {
		return embeddables.get( embeddables.size() - 1 );
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "EmbeddableHierarchy" );
		sb.append( "{defaultAccessType=" ).append( defaultAccessType );
		sb.append( ", embeddables=" ).append( embeddables );
		sb.append( '}' );
		return sb.toString();
	}
}
