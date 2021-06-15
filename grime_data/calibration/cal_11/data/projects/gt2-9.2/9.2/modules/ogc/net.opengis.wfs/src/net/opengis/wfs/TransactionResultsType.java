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
package net.opengis.wfs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transaction Results Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The TransactionResults element may be used to report exception
 *             codes and messages for all actions of a transaction that failed
 *             to complete successfully.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.TransactionResultsType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.TransactionResultsType#getAction <em>Action</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getTransactionResultsType()
 * @model extendedMetaData="name='TransactionResultsType' kind='elementOnly'"
 * @generated
 */
public interface TransactionResultsType extends EObject {
	
	/**
	 * The handle used for the request.
	 * <p>
	 * Adding this to help us maintain backward compatability with wfs 1.0.
	 * </p>
	 * @return The handle given to the transaction request.
	 * 
	 * @model
	 * 
	 * @see TransactionType
	 * @see BaseRequestType#getHandle()
	 */
	String getHandle();
	
	/**
     * Sets the value of the '{@link net.opengis.wfs.TransactionResultsType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
	void setHandle(String value);

	/**
     * Returns the value of the '<em><b>Action</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.ActionType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   The Action element reports an exception code
     *                   and exception message indicating why the
     *                   corresponding action of a transaction request
     *                   failed.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Action</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getTransactionResultsType_Action()
     * @model type="net.opengis.wfs.ActionType" containment="true"
     *        extendedMetaData="kind='element' name='Action' namespace='##targetNamespace'"
     * @generated
     */
	EList getAction();

} // TransactionResultsType
