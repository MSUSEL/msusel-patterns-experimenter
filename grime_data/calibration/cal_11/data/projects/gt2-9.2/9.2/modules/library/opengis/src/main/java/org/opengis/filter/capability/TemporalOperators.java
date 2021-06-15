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
package org.opengis.filter.capability;

import java.util.Collection;

/**
 * Supported temporal operators in a filter capabilities document.
 * 
 * &lt;xsd:simpleType name="TemporalOperatorNameType">
 *    &lt;xsd:union>
 *        &lt;xsd:simpleType>
 *           &lt;xsd:restriction base="xsd:string">
 *              &lt;xsd:enumeration value="After"/>
 *              &lt;xsd:enumeration value="Before"/>
 *              &lt;xsd:enumeration value="Begins"/>
 *              &lt;xsd:enumeration value="BegunBy"/>
 *              &lt;xsd:enumeration value="TContains"/>
 *              &lt;xsd:enumeration value="During"/>
 *              &lt;xsd:enumeration value="TEquals"/>
 *              &lt;xsd:enumeration value="TOverlaps"/>
 *              &lt;xsd:enumeration value="Meets"/>
 *              &lt;xsd:enumeration value="OverlappedBy"/>
 *              &lt;xsd:enumeration value="MetBy"/>
 *              &lt;xsd:enumeration value="Ends"/>
 *              &lt;xsd:enumeration value="EndedBy"/>
 *            &lt;/xsd:restriction>
 *        &lt;/xsd:simpleType>
 *        &lt;xsd:simpleType>
 *           &lt;xsd:restriction base="xsd:string">
 *              &lt;xsd:pattern value="extension:\w{2,}"/>
 *           &lt;/xsd:restriction>
 *        &lt;/xsd:simpleType>
 *     &lt;/xsd:union>
 *  &lt;/xsd:simpleType>
 *
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL$
 */
public interface TemporalOperators {

    /**
     * Provided temporal operators.
     */
    Collection<TemporalOperator> getOperators();

    /**
     * Looks up an operator by name, returning null if no such operator found.
     *
     * @param name the name of the operator.
     *
     * @return The operator, or null.
     */
    TemporalOperator getOperator(String name);
}
