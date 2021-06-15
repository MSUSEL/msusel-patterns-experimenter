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
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;

import java.util.Map;
import java.util.Set;  // For Javadoc
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A list of logically related elements as (<var>name</var>, <var>value</var>) pairs in a
 * dictionary.  A record may be used as an implementation representation for features.
 * <p>
 * This class can be think as the equivalent of the Java {@link Object} class.
 *
 * @author Bryce Nordgren (USDA)
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.1
 *
 * @see RecordType
 *
 *
 * @source $URL$
 */
@UML(identifier="Record", specification=ISO_19103)
public interface Record {
    /**
     * Returns the type definition of record. All attributes named in this record must be defined
     * in the returned record type. In other words, the following relationship must holds:
     * <p>
     * <ul>
     *    <li><code>getRecordType().{@linkplain RecordType#getAttributeTypes
     *        getAttributeTypes()}.{@linkplain Map#keySet keySet()}.{@linkplain
     *        Set#containsAll containsAll}({@linkplain #getAttributes()}.{@linkplain
     *        Map#keySet keySet()})</code></li>
     * </ul>
     * <p>
     * This method can be think as the equivalent of the Java {@link Object#getClass()} method.
     */
    @UML(identifier="recordType", obligation=OPTIONAL, specification=ISO_19103)
    RecordType getRecordType();

    /**
     * Returns the dictionary of all (<var>name</var>, <var>value</var>) pairs in this record.
     * The returned map shall not allows key addition. It may allows the replacement of existing
     * keys only.
     *
     * @see RecordType#getAttributeTypes
     */
    @UML(identifier="attributes", obligation=MANDATORY, specification=ISO_19103)
    Map<MemberName, Object> getAttributes();

    /**
     * Returns the value for an attribute of the specified name. This is functionnaly equivalent
     * to <code>{@linkplain #getAttributes()}.{@linkplain Map#get get}(name)</code>.
     * The type of the returned object is given by
     * <code>{@linkplain #getRecordType()}.{@linkplain RecordType#locate locate}(name)</code>.
     *
     * @see RecordType#locate
     */
    @UML(identifier="locate", obligation=MANDATORY, specification=ISO_19103)
    Object locate(MemberName name);

    /**
     * Set the value for the attribute of the specified name. This is functionally equivalent
     * to <code>{@linkplain #getAttributes()}.{@linkplain Map#put put}(name,value)</code>.
     * Remind that {@code name} keys are constrained to {@linkplain RecordType#getMembers
     * record type members} only.
     *
     * @param  name  The name of the attribute to modify.
     * @param  value The new value for the attribute.
     * @throws UnsupportedOperationException if this record is not modifiable.
     */
    @Extension
    void set(MemberName name, Object value) throws UnsupportedOperationException;
}
