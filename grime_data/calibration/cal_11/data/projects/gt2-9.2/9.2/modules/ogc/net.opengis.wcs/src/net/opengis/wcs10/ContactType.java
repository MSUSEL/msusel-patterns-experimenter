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
package net.opengis.wcs10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contact Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Information required to enable contact with the responsible person and/or organization.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.ContactType#getPhone <em>Phone</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContactType#getAddress <em>Address</em>}</li>
 *   <li>{@link net.opengis.wcs10.ContactType#getOnlineResource <em>Online Resource</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getContactType()
 * @model extendedMetaData="name='ContactType' kind='elementOnly'"
 * @generated
 */
public interface ContactType extends EObject {
    /**
	 * Returns the value of the '<em><b>Phone</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Telephone numbers at which the organization or individual may becontacted.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Phone</em>' containment reference.
	 * @see #setPhone(TelephoneType)
	 * @see net.opengis.wcs10.Wcs10Package#getContactType_Phone()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='phone' namespace='##targetNamespace'"
	 * @generated
	 */
    TelephoneType getPhone();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContactType#getPhone <em>Phone</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Phone</em>' containment reference.
	 * @see #getPhone()
	 * @generated
	 */
    void setPhone(TelephoneType value);

    /**
	 * Returns the value of the '<em><b>Address</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Physical and email address at which the organization or individualmay be contacted.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Address</em>' containment reference.
	 * @see #setAddress(AddressType)
	 * @see net.opengis.wcs10.Wcs10Package#getContactType_Address()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='address' namespace='##targetNamespace'"
	 * @generated
	 */
    AddressType getAddress();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContactType#getAddress <em>Address</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Address</em>' containment reference.
	 * @see #getAddress()
	 * @generated
	 */
    void setAddress(AddressType value);

    /**
	 * Returns the value of the '<em><b>Online Resource</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * On-line information that can be used to contact the individual ororganization.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Online Resource</em>' containment reference.
	 * @see #setOnlineResource(OnlineResourceType)
	 * @see net.opengis.wcs10.Wcs10Package#getContactType_OnlineResource()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='onlineResource' namespace='##targetNamespace'"
	 * @generated
	 */
    OnlineResourceType getOnlineResource();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ContactType#getOnlineResource <em>Online Resource</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Online Resource</em>' containment reference.
	 * @see #getOnlineResource()
	 * @generated
	 */
    void setOnlineResource(OnlineResourceType value);

} // ContactType
