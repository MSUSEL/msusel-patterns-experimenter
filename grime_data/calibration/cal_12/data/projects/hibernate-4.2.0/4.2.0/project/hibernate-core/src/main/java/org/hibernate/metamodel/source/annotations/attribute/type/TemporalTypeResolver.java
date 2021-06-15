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
package org.hibernate.metamodel.source.annotations.attribute.type;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.TemporalType;

import org.jboss.jandex.AnnotationInstance;

import org.hibernate.AnnotationException;
import org.hibernate.AssertionFailure;
import org.hibernate.cfg.NotYetImplementedException;
import org.hibernate.metamodel.source.annotations.JPADotNames;
import org.hibernate.metamodel.source.annotations.JandexHelper;
import org.hibernate.metamodel.source.annotations.attribute.MappedAttribute;
import org.hibernate.type.StandardBasicTypes;

/**
 * @author Strong Liu
 */
public class TemporalTypeResolver extends AbstractAttributeTypeResolver {
	private final MappedAttribute mappedAttribute;
	private final boolean isMapKey;

	public TemporalTypeResolver(MappedAttribute mappedAttribute) {
		if ( mappedAttribute == null ) {
			throw new AssertionFailure( "MappedAttribute is null" );
		}
		this.mappedAttribute = mappedAttribute;
		this.isMapKey = false;//todo
	}

	@Override
	public String resolveHibernateTypeName(AnnotationInstance temporalAnnotation) {

		if ( isTemporalType( mappedAttribute.getAttributeType() ) ) {
			if ( temporalAnnotation == null ) {
				//SPEC 11.1.47 The Temporal annotation must be specified for persistent fields or properties of type java.util.Date and java.util.Calendar.
				throw new AnnotationException( "Attribute " + mappedAttribute.getName() + " is a Temporal type, but no @Temporal annotation found." );
			}
			TemporalType temporalType = JandexHelper.getEnumValue( temporalAnnotation, "value", TemporalType.class );
			boolean isDate = Date.class.isAssignableFrom( mappedAttribute.getAttributeType() );
			String type;
			switch ( temporalType ) {
				case DATE:
					type = isDate ? StandardBasicTypes.DATE.getName() : StandardBasicTypes.CALENDAR_DATE.getName();
					break;
				case TIME:
					type = StandardBasicTypes.TIME.getName();
					if ( !isDate ) {
						throw new NotYetImplementedException( "Calendar cannot persist TIME only" );
					}
					break;
				case TIMESTAMP:
					type = isDate ? StandardBasicTypes.TIMESTAMP.getName() : StandardBasicTypes.CALENDAR.getName();
					break;
				default:
					throw new AssertionFailure( "Unknown temporal type: " + temporalType );
			}
			return type;
		}
		else {
			if ( temporalAnnotation != null ) {
				throw new AnnotationException(
						"@Temporal should only be set on a java.util.Date or java.util.Calendar property: " + mappedAttribute
								.getName()
				);
			}
		}
		return null;
	}

	@Override
	protected AnnotationInstance getTypeDeterminingAnnotationInstance() {
		return JandexHelper.getSingleAnnotation(
				mappedAttribute.annotations(),
				JPADotNames.TEMPORAL
		);
	}

	private static boolean isTemporalType(Class type) {
		return Date.class.isAssignableFrom( type ) || Calendar.class.isAssignableFrom( type );
	}
}
