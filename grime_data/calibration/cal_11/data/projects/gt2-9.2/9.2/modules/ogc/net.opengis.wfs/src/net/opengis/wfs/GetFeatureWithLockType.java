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

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Feature With Lock Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             A GetFeatureWithLock request operates identically to a
 *             GetFeature request expect that it attempts to lock the
 *             feature instances in the result set and includes a lock
 *             identifier in its response to a client.  A lock identifier
 *             is an identifier generated by a Web Feature Service that
 *             a client application can use, in subsequent operations,
 *             to reference the locked set of feature instances.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.GetFeatureWithLockType#getExpiry <em>Expiry</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getGetFeatureWithLockType()
 * @model extendedMetaData="name='GetFeatureWithLockType' kind='elementOnly'"
 * @generated
 */
public interface GetFeatureWithLockType extends GetFeatureType {
	
	/**
     * Returns the value of the '<em><b>Expiry</b></em>' attribute.
     * The default value is <code>"5"</code>.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                      The expiry attribute is used to set the length
     *                      of time (expressed in minutes) that features will
     *                      remain locked as a result of a GetFeatureWithLock
     *                      request.  After the expiry period elapses, the
     *                      locked resources must be released.  If the
     *                      expiry attribute is not set, then the default
     *                      value of 5 minutes will be enforced.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Expiry</em>' attribute.
     * @see #isSetExpiry()
     * @see #unsetExpiry()
     * @see #setExpiry(BigInteger)
     * @see net.opengis.wfs.WfsPackage#getGetFeatureWithLockType_Expiry()
     * @model default="5" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='expiry'"
     * @generated
     */
	BigInteger getExpiry();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GetFeatureWithLockType#getExpiry <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Expiry</em>' attribute.
     * @see #isSetExpiry()
     * @see #unsetExpiry()
     * @see #getExpiry()
     * @generated
     */
	void setExpiry(BigInteger value);

	/**
     * Unsets the value of the '{@link net.opengis.wfs.GetFeatureWithLockType#getExpiry <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetExpiry()
     * @see #getExpiry()
     * @see #setExpiry(BigInteger)
     * @generated
     */
	void unsetExpiry();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.GetFeatureWithLockType#getExpiry <em>Expiry</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Expiry</em>' attribute is set.
     * @see #unsetExpiry()
     * @see #getExpiry()
     * @see #setExpiry(BigInteger)
     * @generated
     */
	boolean isSetExpiry();

} // GetFeatureWithLockType
