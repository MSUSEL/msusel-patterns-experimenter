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
 * A representation of the model object '<em><b>Responsible Party Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identification of, and means of communication with, person(s) and organizations associated with the server.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.ResponsiblePartyType#getIndividualName <em>Individual Name</em>}</li>
 *   <li>{@link net.opengis.wcs10.ResponsiblePartyType#getOrganisationName <em>Organisation Name</em>}</li>
 *   <li>{@link net.opengis.wcs10.ResponsiblePartyType#getOrganisationName1 <em>Organisation Name1</em>}</li>
 *   <li>{@link net.opengis.wcs10.ResponsiblePartyType#getPositionName <em>Position Name</em>}</li>
 *   <li>{@link net.opengis.wcs10.ResponsiblePartyType#getContactInfo <em>Contact Info</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getResponsiblePartyType()
 * @model extendedMetaData="name='ResponsiblePartyType' kind='elementOnly'"
 * @generated
 */
public interface ResponsiblePartyType extends EObject {
    /**
	 * Returns the value of the '<em><b>Individual Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the responsible person-surname, given name, title separated by a delimiter.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Individual Name</em>' attribute.
	 * @see #setIndividualName(String)
	 * @see net.opengis.wcs10.Wcs10Package#getResponsiblePartyType_IndividualName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='individualName' namespace='##targetNamespace'"
	 * @generated
	 */
    String getIndividualName();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ResponsiblePartyType#getIndividualName <em>Individual Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Individual Name</em>' attribute.
	 * @see #getIndividualName()
	 * @generated
	 */
    void setIndividualName(String value);

    /**
	 * Returns the value of the '<em><b>Organisation Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Organisation Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Organisation Name</em>' attribute.
	 * @see #setOrganisationName(String)
	 * @see net.opengis.wcs10.Wcs10Package#getResponsiblePartyType_OrganisationName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='organisationName' namespace='##targetNamespace'"
	 * @generated
	 */
    String getOrganisationName();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ResponsiblePartyType#getOrganisationName <em>Organisation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Organisation Name</em>' attribute.
	 * @see #getOrganisationName()
	 * @generated
	 */
    void setOrganisationName(String value);

    /**
	 * Returns the value of the '<em><b>Organisation Name1</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the responsible organizationt.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Organisation Name1</em>' attribute.
	 * @see #setOrganisationName1(String)
	 * @see net.opengis.wcs10.Wcs10Package#getResponsiblePartyType_OrganisationName1()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='organisationName' namespace='##targetNamespace'"
	 * @generated
	 */
    String getOrganisationName1();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ResponsiblePartyType#getOrganisationName1 <em>Organisation Name1</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Organisation Name1</em>' attribute.
	 * @see #getOrganisationName1()
	 * @generated
	 */
    void setOrganisationName1(String value);

    /**
	 * Returns the value of the '<em><b>Position Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Role or position of the responsible person.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Position Name</em>' attribute.
	 * @see #setPositionName(String)
	 * @see net.opengis.wcs10.Wcs10Package#getResponsiblePartyType_PositionName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='positionName' namespace='##targetNamespace'"
	 * @generated
	 */
    String getPositionName();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ResponsiblePartyType#getPositionName <em>Position Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Position Name</em>' attribute.
	 * @see #getPositionName()
	 * @generated
	 */
    void setPositionName(String value);

    /**
	 * Returns the value of the '<em><b>Contact Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Address of the responsible party.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Contact Info</em>' containment reference.
	 * @see #setContactInfo(ContactType)
	 * @see net.opengis.wcs10.Wcs10Package#getResponsiblePartyType_ContactInfo()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='contactInfo' namespace='##targetNamespace'"
	 * @generated
	 */
    ContactType getContactInfo();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ResponsiblePartyType#getContactInfo <em>Contact Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contact Info</em>' containment reference.
	 * @see #getContactInfo()
	 * @generated
	 */
    void setContactInfo(ContactType value);

} // ResponsiblePartyType
