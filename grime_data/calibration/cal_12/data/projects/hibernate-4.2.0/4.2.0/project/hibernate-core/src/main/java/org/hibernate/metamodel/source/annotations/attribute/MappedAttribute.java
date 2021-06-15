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
package org.hibernate.metamodel.source.annotations.attribute;

import java.util.List;
import java.util.Map;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.DotName;

import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.source.annotations.HibernateDotNames;
import org.hibernate.metamodel.source.annotations.JPADotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;
import org.hibernate.metamodel.source.annotations.attribute.type.AttributeTypeResolver;
import org.hibernate.metamodel.source.annotations.entity.EntityBindingContext;

/**
 * Base class for the different types of mapped attributes
 *
 * @author Hardy Ferentschik
 */
public abstract class MappedAttribute implements Comparable<MappedAttribute> {
	/**
	 * Annotations defined on the attribute, keyed against the annotation dot name.
	 */
	private final Map<DotName, List<AnnotationInstance>> annotations;

	/**
	 * The property name.
	 */
	private final String name;

	/**
	 * The java type of the attribute
	 */
	private final Class<?> attributeType;

	/**
	 * The access type for this property. At the moment this is either 'field' or 'property', but Hibernate
	 * also allows custom named accessors (see {@link org.hibernate.property.PropertyAccessorFactory}).
	 */
	private final String accessType;

	/**
	 * Defines the column values (relational values) for this property.
	 */
	private ColumnValues columnValues;

	/**
	 * Is this property an id property (or part thereof).
	 */
	private final boolean isId;

	/**
	 * Whether a change of the property's value triggers a version increment of the entity (in case of optimistic
	 * locking).
	 */
	private final boolean isOptimisticLockable;

	/**
	 * The binding context
	 */
	private final EntityBindingContext context;

	MappedAttribute(String name, Class<?> attributeType, String accessType, Map<DotName, List<AnnotationInstance>> annotations, EntityBindingContext context) {
		this.context = context;
		this.annotations = annotations;
		this.name = name;
		this.attributeType = attributeType;
		this.accessType = accessType;

		//if this attribute has either @Id or @EmbeddedId, then it is an id attribute
		AnnotationInstance idAnnotation = JandexHelper.getSingleAnnotation( annotations, JPADotNames.ID );
		AnnotationInstance embeddedIdAnnotation = JandexHelper.getSingleAnnotation(
				annotations,
				JPADotNames.EMBEDDED_ID
		);
		isId = ( idAnnotation != null || embeddedIdAnnotation != null );

		AnnotationInstance columnAnnotation = JandexHelper.getSingleAnnotation(
				annotations,
				JPADotNames.COLUMN
		);
		columnValues = new ColumnValues( columnAnnotation );

		this.isOptimisticLockable = checkOptimisticLockAnnotation();
	}

	public String getName() {
		return name;
	}

	public final Class<?> getAttributeType() {
		return attributeType;
	}

	public String getAccessType() {
		return accessType;
	}

	public EntityBindingContext getContext() {
		return context;
	}

	public Map<DotName, List<AnnotationInstance>> annotations() {
		return annotations;
	}

	public ColumnValues getColumnValues() {
		return columnValues;
	}

	public boolean isId() {
		return isId;
	}

	public boolean isOptimisticLockable() {
		return isOptimisticLockable;
	}

	@Override
	public int compareTo(MappedAttribute mappedProperty) {
		return name.compareTo( mappedProperty.getName() );
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "MappedAttribute" );
		sb.append( "{name='" ).append( name ).append( '\'' );
		sb.append( '}' );
		return sb.toString();
	}

	public abstract AttributeTypeResolver getHibernateTypeResolver();

	public abstract boolean isLazy();

	public abstract boolean isOptional();

	public abstract boolean isInsertable();

	public abstract boolean isUpdatable();

	public abstract PropertyGeneration getPropertyGeneration();

	private boolean checkOptimisticLockAnnotation() {
		boolean triggersVersionIncrement = true;
		AnnotationInstance optimisticLockAnnotation = JandexHelper.getSingleAnnotation(
				annotations(),
				HibernateDotNames.OPTIMISTIC_LOCK
		);
		if ( optimisticLockAnnotation != null ) {
			boolean exclude = optimisticLockAnnotation.value( "excluded" ).asBoolean();
			triggersVersionIncrement = !exclude;
		}
		return triggersVersionIncrement;
	}
}


