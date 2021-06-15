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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.ServiceMetadataType#getFormatSupported <em>Format Supported</em>}</li>
 *   <li>{@link net.opengis.wcs20.ServiceMetadataType#getExtension <em>Extension</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getServiceMetadataType()
 * @model extendedMetaData="name='ServiceMetadataType' kind='elementOnly'"
 * @generated
 */
public interface ServiceMetadataType extends EObject {
    /**
     * Returns the value of the '<em><b>Format Supported</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Format Supported</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Format Supported</em>' attribute.
     * @see #setFormatSupported(String)
     * @see net.opengis.wcs20.Wcs20Package#getServiceMetadataType_FormatSupported()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='formatSupported' namespace='##targetNamespace'"
     * @generated
     */
    String getFormatSupported();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ServiceMetadataType#getFormatSupported <em>Format Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format Supported</em>' attribute.
     * @see #getFormatSupported()
     * @generated
     */
    void setFormatSupported(String value);

    /**
     * Returns the value of the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Extension element used to hook in additional content e.g. in extensions or application profiles.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Extension</em>' containment reference.
     * @see #setExtension(ExtensionType)
     * @see net.opengis.wcs20.Wcs20Package#getServiceMetadataType_Extension()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Extension' namespace='##targetNamespace'"
     * @generated
     */
    ExtensionType getExtension();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ServiceMetadataType#getExtension <em>Extension</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extension</em>' containment reference.
     * @see #getExtension()
     * @generated
     */
    void setExtension(ExtensionType value);

} // ServiceMetadataType
