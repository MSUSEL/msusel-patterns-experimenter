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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.type;

import java.util.Collection;
import java.util.List;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

/**
 * Factory for types and descriptors.
 * <p>
 * Implementations of this interface should not contain any "special logic" for
 * creating types. Method implementations should be straight through calls to a
 * constructor.
 * </p>
 * @author Gabriel Roldan (Axios Engineering)
 * @author Justin Deoliveira (The Open Planning Project)
 *
 *
 * @source $URL$
 */
public interface FeatureTypeFactory {
    /**
     * Creates a schema.
     *
     * @param namespaceURI The uri of the schema.
     */
    Schema createSchema(String namespaceURI);

    /**
     * Creates an association descriptor.
     *
     * @param type
     *  The type of the described association.
     * @param name
     *  The name of the described association.
     * @param minOccurs
     *  The minimum number of occurences of the described association.
     * @param maxOCcurs
     *  The maximum number of occurences of the described association.
     * @param isNillable
     *  Flag indicating wether the association is allowed to be <code>null</code>.
     */
    AssociationDescriptor createAssociationDescriptor(
        AssociationType type, Name name, int minOccurs, int maxOCcurs,
        boolean isNillable
    );

    /**
     * Creates an attribute descriptor.
     *
     * @param type
     *  The type of the described attribute.
     * @param name
     *  The name of the described attribute.
     * @param minOccurs
     *  The minimum number of occurences of the described attribute.
     * @param maxOccurs
     *  The maximum number of occurences of the described attribute.
     * @param isNillable
     *  Flag indicating if the described attribute may have a null value.
     * @param defaulValue
     *  The default value of the described attribute.
     */
    AttributeDescriptor createAttributeDescriptor(
        AttributeType type, Name name, int minOccurs, int maxOccurs,
        boolean isNillable, Object defaultValue
    );

    /**
     * Creates a geometry descriptor.
     *
     * @param type
     *  The type of the described attribute.
     * @param name
     *  The name of the described attribute.
     * @param minOccurs
     *  The minimum number of occurences of the described attribute.
     * @param maxOccurs
     *  The maximum number of occurences of the described attribute.
     * @param isNillable
     *  Flag indicating if the described attribute may have a null value.
     * @param defaulValue
     *  The default value of the described attribute.
     */
    GeometryDescriptor createGeometryDescriptor(
        GeometryType type, Name name, int minOccurs, int maxOccurs,
        boolean isNillable, Object defaultValue
    );

    /**
     * Creates an association type.
     *
     * @param name
     *  The name of the type.
     * @param relatedType
     *  The type of attributes referenced by the association.
     * @param isAbstract
     *  Flag indicating if the type is abstract.
     * @param restrictions
     *  Set of restrictions on the association.
     * @param superType
     *  Parent type.
     * @param description
     *  A description of the type..
     */
    AssociationType createAssociationType(
        Name name, AttributeType relatedType, boolean isAbstract,
        List<Filter> restrictions, AssociationType superType,
        InternationalString description
    );

    /**
     * Creates an attribute type.
     *
     * @param name
     *  The name of the type.
     * @param binding
     *  The class that values of attributes of the type.
     * @param isIdentifiable
     *  Flag indicating if the attribute is identifiable.
     * @param isAbstract
     *  Flag indicating if the type is abstract.
     * @param restrictions
     *  Set of restrictions on the attribute.
     * @param superType
     *  Parent type.
     * @param description
     *  A description of the type.
     */
    AttributeType createAttributeType(
        Name name, Class<?> binding, boolean isIdentifiable, boolean isAbstract,
        List<Filter> restrictions, AttributeType superType, InternationalString description
    );

    /**
     * Creates a geometric attribute type.
     *
     * @param name
     *  The name of the type.
     * @param binding
     *  The class of values of attributes of the type.
     * @param crs
     *  The coordinate reference system of the type.
     * @param isIdentifiable
     *  Flag indicating if the attribute is identifiable.
     * @param isAbstract
     *  Flag indicating if the type is abstract.
     * @param restrictions
     *  Set of restrictions on the attribute.
     * @param superType
     *  Parent type.
     * @param description
     *  A description of the type.
     */
    GeometryType createGeometryType(
        Name name, Class<?> binding, CoordinateReferenceSystem crs, boolean isIdentifiable,
        boolean isAbstract, List<Filter> restrictions, AttributeType superType,
        InternationalString description
    );

    /**
     * Creates a complex type.
     *
     * @param name
     *  The name of the type.
     * @param schema
     *  Collection of property descriptors which define the type.
     * @param isIdentifiable
     *  Flag indicating if the attribute is identifiable.
     * @param isAbstract
     *  Flag indicating if the type is abstract.
     * @param restrictions
     *  Set of restrictions on the attribute.
     * @param superType
     *  Parent type.
     * @param description
     *  A description of the type.
     */
    ComplexType createComplexType(
        Name name, Collection<PropertyDescriptor> schema, boolean isIdentifiable,
        boolean isAbstract, List<Filter> restrictions, AttributeType superType,
        InternationalString description
    );

    /**
     * Creates a feature type.
     *
     * @param name
     *  The name of the type.
     * @param schema
     *  Collection of property descriptors which define the type.
     * @param isAbstract
     *  Flag indicating if the type is abstract.
     * @param restrictions
     *  Set of restrictions on the attribute.
     * @param superType
     *  Parent type.
     * @param description
     *  A description of the type.
     */
    FeatureType createFeatureType(
        Name name, Collection<PropertyDescriptor> schema,
        GeometryDescriptor defaultGeometry, boolean isAbstract,
        List<Filter> restrictions, AttributeType superType, InternationalString description
    );

    /**
     * Creates a simple feature type.
     *
     * @param name
     *  The name of the type.
     * @param schema
     *  List of attribute descriptors which define the type.
     * @param isAbstract
     *  Flag indicating if the type is abstract.
     * @param restrictions
     *  Set of restrictions on the attribute.
     * @param superType
     *  Parent type.
     * @param description
     *  A description of the type.
     */
    SimpleFeatureType createSimpleFeatureType(
        Name name, List<AttributeDescriptor> schema, GeometryDescriptor defaultGeometry,
        boolean isAbstract, List<Filter> restrictions, AttributeType superType,
        InternationalString description
    );
}
