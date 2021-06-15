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
package net.opengis.ows10;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.ows10.Ows10Package
 * @generated
 */
public interface Ows10Factory extends EFactory {
	/**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	Ows10Factory eINSTANCE = net.opengis.ows10.impl.Ows10FactoryImpl.init();

	/**
     * Returns a new object of class '<em>Accept Formats Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Accept Formats Type</em>'.
     * @generated
     */
	AcceptFormatsType createAcceptFormatsType();

	/**
     * Returns a new object of class '<em>Accept Versions Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Accept Versions Type</em>'.
     * @generated
     */
	AcceptVersionsType createAcceptVersionsType();

	/**
     * Returns a new object of class '<em>Address Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Address Type</em>'.
     * @generated
     */
	AddressType createAddressType();

	/**
     * Returns a new object of class '<em>Bounding Box Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Bounding Box Type</em>'.
     * @generated
     */
	BoundingBoxType createBoundingBoxType();

	/**
     * Returns a new object of class '<em>Capabilities Base Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Capabilities Base Type</em>'.
     * @generated
     */
	CapabilitiesBaseType createCapabilitiesBaseType();

	/**
     * Returns a new object of class '<em>Code Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Code Type</em>'.
     * @generated
     */
	CodeType createCodeType();

	/**
     * Returns a new object of class '<em>Contact Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Contact Type</em>'.
     * @generated
     */
	ContactType createContactType();

	/**
     * Returns a new object of class '<em>DCP Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>DCP Type</em>'.
     * @generated
     */
	DCPType createDCPType();

	/**
     * Returns a new object of class '<em>Description Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Description Type</em>'.
     * @generated
     */
	DescriptionType createDescriptionType();

	/**
     * Returns a new object of class '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Document Root</em>'.
     * @generated
     */
	DocumentRoot createDocumentRoot();

	/**
     * Returns a new object of class '<em>Domain Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Domain Type</em>'.
     * @generated
     */
	DomainType createDomainType();

	/**
     * Returns a new object of class '<em>Exception Report Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Exception Report Type</em>'.
     * @generated
     */
	ExceptionReportType createExceptionReportType();

	/**
     * Returns a new object of class '<em>Exception Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Exception Type</em>'.
     * @generated
     */
	ExceptionType createExceptionType();

	/**
     * Returns a new object of class '<em>Get Capabilities Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Get Capabilities Type</em>'.
     * @generated
     */
	GetCapabilitiesType createGetCapabilitiesType();

	/**
     * Returns a new object of class '<em>HTTP Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>HTTP Type</em>'.
     * @generated
     */
	HTTPType createHTTPType();

	/**
     * Returns a new object of class '<em>Identification Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Identification Type</em>'.
     * @generated
     */
	IdentificationType createIdentificationType();

	/**
     * Returns a new object of class '<em>Keywords Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Keywords Type</em>'.
     * @generated
     */
	KeywordsType createKeywordsType();

	/**
     * Returns a new object of class '<em>Metadata Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Metadata Type</em>'.
     * @generated
     */
	MetadataType createMetadataType();

	/**
     * Returns a new object of class '<em>Online Resource Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Online Resource Type</em>'.
     * @generated
     */
	OnlineResourceType createOnlineResourceType();

	/**
     * Returns a new object of class '<em>Operation Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Operation Type</em>'.
     * @generated
     */
	OperationType createOperationType();

	/**
     * Returns a new object of class '<em>Operations Metadata Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Operations Metadata Type</em>'.
     * @generated
     */
	OperationsMetadataType createOperationsMetadataType();

	/**
     * Returns a new object of class '<em>Request Method Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Request Method Type</em>'.
     * @generated
     */
	RequestMethodType createRequestMethodType();

	/**
     * Returns a new object of class '<em>Responsible Party Subset Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Responsible Party Subset Type</em>'.
     * @generated
     */
	ResponsiblePartySubsetType createResponsiblePartySubsetType();

	/**
     * Returns a new object of class '<em>Responsible Party Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Responsible Party Type</em>'.
     * @generated
     */
	ResponsiblePartyType createResponsiblePartyType();

	/**
     * Returns a new object of class '<em>Sections Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Sections Type</em>'.
     * @generated
     */
	SectionsType createSectionsType();

	/**
     * Returns a new object of class '<em>Service Identification Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Service Identification Type</em>'.
     * @generated
     */
	ServiceIdentificationType createServiceIdentificationType();

	/**
     * Returns a new object of class '<em>Service Provider Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Service Provider Type</em>'.
     * @generated
     */
	ServiceProviderType createServiceProviderType();

	/**
     * Returns a new object of class '<em>Telephone Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>Telephone Type</em>'.
     * @generated
     */
	TelephoneType createTelephoneType();

	/**
     * Returns a new object of class '<em>WGS84 Bounding Box Type</em>'.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return a new object of class '<em>WGS84 Bounding Box Type</em>'.
     * @generated
     */
	WGS84BoundingBoxType createWGS84BoundingBoxType();

	/**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
	Ows10Package getOws10Package();

} //Ows10Factory
