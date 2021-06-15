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
package net.opengis.wfs20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transaction Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.TransactionResponseType#getTransactionSummary <em>Transaction Summary</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionResponseType#getInsertResults <em>Insert Results</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionResponseType#getUpdateResults <em>Update Results</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionResponseType#getReplaceResults <em>Replace Results</em>}</li>
 *   <li>{@link net.opengis.wfs20.TransactionResponseType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getTransactionResponseType()
 * @model extendedMetaData="name='TransactionResponseType' kind='elementOnly'"
 * @generated
 */
public interface TransactionResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Transaction Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Transaction Summary</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Transaction Summary</em>' containment reference.
     * @see #setTransactionSummary(TransactionSummaryType)
     * @see net.opengis.wfs20.Wfs20Package#getTransactionResponseType_TransactionSummary()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TransactionSummary' namespace='##targetNamespace'"
     * @generated
     */
    TransactionSummaryType getTransactionSummary();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TransactionResponseType#getTransactionSummary <em>Transaction Summary</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transaction Summary</em>' containment reference.
     * @see #getTransactionSummary()
     * @generated
     */
    void setTransactionSummary(TransactionSummaryType value);

    /**
     * Returns the value of the '<em><b>Insert Results</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Insert Results</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Insert Results</em>' containment reference.
     * @see #setInsertResults(ActionResultsType)
     * @see net.opengis.wfs20.Wfs20Package#getTransactionResponseType_InsertResults()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='InsertResults' namespace='##targetNamespace'"
     * @generated
     */
    ActionResultsType getInsertResults();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TransactionResponseType#getInsertResults <em>Insert Results</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Insert Results</em>' containment reference.
     * @see #getInsertResults()
     * @generated
     */
    void setInsertResults(ActionResultsType value);

    /**
     * Returns the value of the '<em><b>Update Results</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Update Results</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Update Results</em>' containment reference.
     * @see #setUpdateResults(ActionResultsType)
     * @see net.opengis.wfs20.Wfs20Package#getTransactionResponseType_UpdateResults()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='UpdateResults' namespace='##targetNamespace'"
     * @generated
     */
    ActionResultsType getUpdateResults();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TransactionResponseType#getUpdateResults <em>Update Results</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Update Results</em>' containment reference.
     * @see #getUpdateResults()
     * @generated
     */
    void setUpdateResults(ActionResultsType value);

    /**
     * Returns the value of the '<em><b>Replace Results</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Replace Results</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Replace Results</em>' containment reference.
     * @see #setReplaceResults(ActionResultsType)
     * @see net.opengis.wfs20.Wfs20Package#getTransactionResponseType_ReplaceResults()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ReplaceResults' namespace='##targetNamespace'"
     * @generated
     */
    ActionResultsType getReplaceResults();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TransactionResponseType#getReplaceResults <em>Replace Results</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Replace Results</em>' containment reference.
     * @see #getReplaceResults()
     * @generated
     */
    void setReplaceResults(ActionResultsType value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * The default value is <code>"2.0.0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #setVersion(String)
     * @see net.opengis.wfs20.Wfs20Package#getTransactionResponseType_Version()
     * @model default="2.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TransactionResponseType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.TransactionResponseType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    void unsetVersion();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.TransactionResponseType#getVersion <em>Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Version</em>' attribute is set.
     * @see #unsetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    boolean isSetVersion();

} // TransactionResponseType
