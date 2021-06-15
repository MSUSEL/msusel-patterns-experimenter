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
package net.opengis.wps10;

import java.math.BigInteger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Supported Complex Data Input Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.SupportedComplexDataInputType#getMaximumMegabytes <em>Maximum Megabytes</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getSupportedComplexDataInputType()
 * @model extendedMetaData="name='SupportedComplexDataInputType' kind='elementOnly'"
 * @generated
 */
public interface SupportedComplexDataInputType extends SupportedComplexDataType {
    /**
     * Returns the value of the '<em><b>Maximum Megabytes</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The maximum file size, in megabytes, of this input.  If the input exceeds this size, the server will return an error instead of processing the inputs.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Maximum Megabytes</em>' attribute.
     * @see #setMaximumMegabytes(BigInteger)
     * @see net.opengis.wps10.Wps10Package#getSupportedComplexDataInputType_MaximumMegabytes()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
     *        extendedMetaData="kind='attribute' name='maximumMegabytes'"
     * @generated
     */
    BigInteger getMaximumMegabytes();

    /**
     * Sets the value of the '{@link net.opengis.wps10.SupportedComplexDataInputType#getMaximumMegabytes <em>Maximum Megabytes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Maximum Megabytes</em>' attribute.
     * @see #getMaximumMegabytes()
     * @generated
     */
    void setMaximumMegabytes(BigInteger value);

} // SupportedComplexDataInputType
