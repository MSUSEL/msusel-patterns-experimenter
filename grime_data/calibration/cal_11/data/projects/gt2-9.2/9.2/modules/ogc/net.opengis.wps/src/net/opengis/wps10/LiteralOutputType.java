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

import net.opengis.ows11.DomainMetadataType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Literal Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of a literal output (or input).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.LiteralOutputType#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wps10.LiteralOutputType#getUOMs <em>UO Ms</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getLiteralOutputType()
 * @model extendedMetaData="name='LiteralOutputType' kind='elementOnly'"
 * @generated
 */
public interface LiteralOutputType extends EObject {
    /**
     * Returns the value of the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Data type of this set of values (e.g. integer, real, etc). This data type metadata should be included for each quantity whose data type is not a string.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Type</em>' containment reference.
     * @see #setDataType(DomainMetadataType)
     * @see net.opengis.wps10.Wps10Package#getLiteralOutputType_DataType()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DataType' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    DomainMetadataType getDataType();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralOutputType#getDataType <em>Data Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Type</em>' containment reference.
     * @see #getDataType()
     * @generated
     */
    void setDataType(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>UO Ms</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of supported units of measure for this input or output. This element should be included when this literal has a unit of measure (e.g., "meters", without a more complete reference system). Not necessary for a count, which has no units.
     * <!-- end-model-doc -->
     * @return the value of the '<em>UO Ms</em>' containment reference.
     * @see #setUOMs(SupportedUOMsType)
     * @see net.opengis.wps10.Wps10Package#getLiteralOutputType_UOMs()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='UOMs'"
     * @generated
     */
    SupportedUOMsType getUOMs();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralOutputType#getUOMs <em>UO Ms</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>UO Ms</em>' containment reference.
     * @see #getUOMs()
     * @generated
     */
    void setUOMs(SupportedUOMsType value);

} // LiteralOutputType
