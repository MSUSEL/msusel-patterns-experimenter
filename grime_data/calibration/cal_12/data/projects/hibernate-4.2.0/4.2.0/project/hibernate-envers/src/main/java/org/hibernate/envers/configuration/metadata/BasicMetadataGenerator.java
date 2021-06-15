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
package org.hibernate.envers.configuration.metadata;

import java.util.Properties;

import org.dom4j.Element;

import org.hibernate.envers.configuration.metadata.reader.PropertyAuditingData;
import org.hibernate.envers.entities.mapper.SimpleMapperBuilder;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Value;
import org.hibernate.type.BasicType;
import org.hibernate.type.CustomType;
import org.hibernate.type.EnumType;
import org.hibernate.type.SerializableToBlobType;
import org.hibernate.type.Type;
import org.hibernate.usertype.DynamicParameterizedType;

/**
 * Generates metadata for basic properties: immutable types (including enums).
 * @author Adam Warski (adam at warski dot org)
 */
public final class BasicMetadataGenerator {
	@SuppressWarnings({ "unchecked" })
	boolean addBasic(Element parent, PropertyAuditingData propertyAuditingData,
					 Value value, SimpleMapperBuilder mapper, boolean insertable, boolean key) {
		Type type = value.getType();

		if ( type instanceof BasicType || type instanceof SerializableToBlobType ||
				"org.hibernate.type.PrimitiveByteArrayBlobType".equals( type.getClass().getName() ) ) {
			if ( parent != null ) {
				boolean addNestedType = ( value instanceof SimpleValue ) && ( (SimpleValue) value ).getTypeParameters() != null;

				String typeName = type.getName();
				if ( typeName == null ) {
					typeName = type.getClass().getName();
				}

				Element prop_mapping = MetadataTools.addProperty(
						parent, propertyAuditingData.getName(),
						addNestedType ? null : typeName, propertyAuditingData.isForceInsertable() || insertable, key
				);
				MetadataTools.addColumns( prop_mapping, value.getColumnIterator() );

				if ( addNestedType ) {
					Properties typeParameters = ( (SimpleValue) value ).getTypeParameters();
					Element type_mapping = prop_mapping.addElement( "type" );
					type_mapping.addAttribute( "name", typeName );

					if ( "org.hibernate.type.EnumType".equals( typeName ) ) {
						// Proper handling of enumeration type
						mapEnumerationType( type_mapping, type, typeParameters );
					}
					else {
						// By default copying all Hibernate properties
						for ( Object object : typeParameters.keySet() ) {
							String keyType = (String) object;
							String property = typeParameters.getProperty( keyType );

							if ( property != null ) {
								type_mapping.addElement( "param" ).addAttribute( "name", keyType ).setText( property );
							}
						}
					}
				}
			}

			// A null mapper means that we only want to add xml mappings
			if ( mapper != null ) {
				mapper.add( propertyAuditingData.getPropertyData() );
			}
		}
		else {
			return false;
		}

		return true;
	}

	private void mapEnumerationType(Element parent, Type type, Properties parameters) {
		if ( parameters.getProperty( EnumType.ENUM ) != null ) {
			parent.addElement( "param" ).addAttribute( "name", EnumType.ENUM ).setText( parameters.getProperty( EnumType.ENUM ) );
		}
		else {
			parent.addElement( "param" ).addAttribute( "name", EnumType.ENUM ).setText( type.getReturnedClass().getName() );
		}
		if ( parameters.getProperty( EnumType.NAMED ) != null ) {
			parent.addElement( "param" ).addAttribute( "name", EnumType.NAMED ).setText( parameters.getProperty( EnumType.NAMED ) );
		}
		else if ( parameters.get( DynamicParameterizedType.XPROPERTY ) != null ) {
			// Case of annotations.
			parent.addElement( "param" ).addAttribute( "name", EnumType.NAMED )
										.setText( "" + !( (EnumType) ( (CustomType) type ).getUserType() ).isOrdinal() );
		}
		else {
			// Otherwise we assume that the choice between ordinal and named representation has been omitted.
			// Call to EnumType#isOrdinal() would always return the default Types.INTEGER. We let Hibernate
			// to choose the proper strategy during runtime.
		}
	}

	@SuppressWarnings({ "unchecked" })
	boolean addManyToOne(Element parent, PropertyAuditingData propertyAuditingData, Value value, SimpleMapperBuilder mapper) {
		Type type = value.getType();

		// A null mapper occurs when adding to composite-id element
		Element manyToOneElement = parent.addElement( mapper != null ? "many-to-one" : "key-many-to-one" );
		manyToOneElement.addAttribute( "name", propertyAuditingData.getName() );
		manyToOneElement.addAttribute( "class", type.getName() );
		MetadataTools.addColumns( manyToOneElement, value.getColumnIterator() );

		// A null mapper means that we only want to add xml mappings
		if ( mapper != null ) {
			mapper.add( propertyAuditingData.getPropertyData() );
		}

		return true;
	}
}
