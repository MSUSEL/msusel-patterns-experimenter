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
package net.opengis.ows11;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * XML encoded GetCapabilities operation request. This operation allows clients to retrieve service metadata about a specific service instance. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation. This base type shall be extended by each specific OWS to include the additional required "service" attribute, with the correct value for that OWS.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}</li>
 *   <li>{@link net.opengis.ows11.GetCapabilitiesType#getSections <em>Sections</em>}</li>
 *   <li>{@link net.opengis.ows11.GetCapabilitiesType#getAcceptFormats <em>Accept Formats</em>}</li>
 *   <li>{@link net.opengis.ows11.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.ows11.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link net.opengis.ows11.GetCapabilitiesType#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link net.opengis.ows11.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getGetCapabilitiesType()
 * @model extendedMetaData="name='GetCapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface GetCapabilitiesType extends EObject {
    /**
	 * Returns the value of the '<em><b>Accept Versions</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * When omitted, server shall return latest supported version.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Accept Versions</em>' containment reference.
	 * @see #setAcceptVersions(AcceptVersionsType)
	 * @see net.opengis.ows11.Ows11Package#getGetCapabilitiesType_AcceptVersions()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='AcceptVersions' namespace='##targetNamespace'"
	 * @generated
	 */
    AcceptVersionsType getAcceptVersions();

    /**
	 * Sets the value of the '{@link net.opengis.ows11.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Accept Versions</em>' containment reference.
	 * @see #getAcceptVersions()
	 * @generated
	 */
    void setAcceptVersions(AcceptVersionsType value);

    /**
	 * Returns the value of the '<em><b>Sections</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * When omitted or not supported by server, server shall return complete service metadata (Capabilities) document.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sections</em>' containment reference.
	 * @see #setSections(SectionsType)
	 * @see net.opengis.ows11.Ows11Package#getGetCapabilitiesType_Sections()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Sections' namespace='##targetNamespace'"
	 * @generated
	 */
    SectionsType getSections();

    /**
	 * Sets the value of the '{@link net.opengis.ows11.GetCapabilitiesType#getSections <em>Sections</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sections</em>' containment reference.
	 * @see #getSections()
	 * @generated
	 */
    void setSections(SectionsType value);

    /**
	 * Returns the value of the '<em><b>Accept Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * When omitted or not supported by server, server shall return service metadata document using the MIME type "text/xml".
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Accept Formats</em>' containment reference.
	 * @see #setAcceptFormats(AcceptFormatsType)
	 * @see net.opengis.ows11.Ows11Package#getGetCapabilitiesType_AcceptFormats()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='AcceptFormats' namespace='##targetNamespace'"
	 * @generated
	 */
    AcceptFormatsType getAcceptFormats();

    /**
	 * Sets the value of the '{@link net.opengis.ows11.GetCapabilitiesType#getAcceptFormats <em>Accept Formats</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Accept Formats</em>' containment reference.
	 * @see #getAcceptFormats()
	 * @generated
	 */
    void setAcceptFormats(AcceptFormatsType value);

    /**
	 * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * When omitted or not supported by server, server shall return latest complete service metadata document.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update Sequence</em>' attribute.
	 * @see #setUpdateSequence(String)
	 * @see net.opengis.ows11.Ows11Package#getGetCapabilitiesType_UpdateSequence()
	 * @model dataType="net.opengis.ows11.UpdateSequenceType"
	 *        extendedMetaData="kind='attribute' name='updateSequence'"
	 * @generated
	 */
    String getUpdateSequence();

    /**
	 * Sets the value of the '{@link net.opengis.ows11.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update Sequence</em>' attribute.
	 * @see #getUpdateSequence()
	 * @generated
	 */
    void setUpdateSequence(String value);

    /**
	 * Returns the value of the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Url</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Url</em>' attribute.
	 * @see #setBaseUrl(String)
	 * @see net.opengis.ows11.Ows11Package#getGetCapabilitiesType_BaseUrl()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
    String getBaseUrl();

    /**
	 * Sets the value of the '{@link net.opengis.ows11.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Url</em>' attribute.
	 * @see #getBaseUrl()
	 * @generated
	 */
    void setBaseUrl(String value);

    /**
	 * Returns the value of the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Namespace</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Namespace</em>' attribute.
	 * @see #setNamespace(String)
	 * @see net.opengis.ows11.Ows11Package#getGetCapabilitiesType_Namespace()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
    String getNamespace();

    /**
	 * Sets the value of the '{@link net.opengis.ows11.GetCapabilitiesType#getNamespace <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Namespace</em>' attribute.
	 * @see #getNamespace()
	 * @generated
	 */
    void setNamespace(String value);

    /**
     * Extended model properties.
     * <p>
     * This map allows client to store additional properties with the this
     * request object, properties that are not part of the model proper.
     * </p>
     * 
     * @model
     */
    Map getExtendedProperties();

				/**
	 * Sets the value of the '{@link net.opengis.ows11.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extended Properties</em>' attribute.
	 * @see #getExtendedProperties()
	 * @generated
	 */
	void setExtendedProperties(Map value);

} // GetCapabilitiesType
