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
/**
 */
package net.opengis.wcs20;

import net.opengis.ows20.CapabilitiesBaseType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.CapabilitiesType#getServiceMetadata <em>Service Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs20.CapabilitiesType#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getCapabilitiesType()
 * @model extendedMetaData="name='CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface CapabilitiesType extends CapabilitiesBaseType {
    /**
     * Returns the value of the '<em><b>Service Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Despite its name this element should not be confuse with the OWSServiceMetadata defined in OWS Common.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Metadata</em>' containment reference.
     * @see #setServiceMetadata(ServiceMetadataType)
     * @see net.opengis.wcs20.Wcs20Package#getCapabilitiesType_ServiceMetadata()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ServiceMetadata' namespace='##targetNamespace'"
     * @generated
     */
    ServiceMetadataType getServiceMetadata();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CapabilitiesType#getServiceMetadata <em>Service Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Metadata</em>' containment reference.
     * @see #getServiceMetadata()
     * @generated
     */
    void setServiceMetadata(ServiceMetadataType value);

    /**
     * Returns the value of the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element redefines the OWS Common [OGC 06-121r9] Contents section with a CoverageSummary, in accordance with the rules for modification laid down there. In addition it allows WCS extensions or application profiles to extend its content.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Contents</em>' containment reference.
     * @see #setContents(ContentsType)
     * @see net.opengis.wcs20.Wcs20Package#getCapabilitiesType_Contents()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Contents' namespace='##targetNamespace'"
     * @generated
     */
    ContentsType getContents();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CapabilitiesType#getContents <em>Contents</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Contents</em>' containment reference.
     * @see #getContents()
     * @generated
     */
    void setContents(ContentsType value);

} // CapabilitiesType
