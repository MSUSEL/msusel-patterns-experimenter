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
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sort Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.SortPropertyType#getValueReference <em>Value Reference</em>}</li>
 *   <li>{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getSortPropertyType()
 * @model extendedMetaData="name='SortPropertyType' kind='elementOnly'"
 * @generated
 */
public interface SortPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Value Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value Reference</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Reference</em>' attribute.
     * @see #setValueReference(String)
     * @see net.opengis.fes20.Fes20Package#getSortPropertyType_ValueReference()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='ValueReference' namespace='##targetNamespace'"
     * @generated
     */
    String getValueReference();

    /**
     * Sets the value of the '{@link net.opengis.fes20.SortPropertyType#getValueReference <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Reference</em>' attribute.
     * @see #getValueReference()
     * @generated
     */
    void setValueReference(String value);

    /**
     * Returns the value of the '<em><b>Sort Order</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.fes20.SortOrderType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sort Order</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sort Order</em>' attribute.
     * @see net.opengis.fes20.SortOrderType
     * @see #isSetSortOrder()
     * @see #unsetSortOrder()
     * @see #setSortOrder(SortOrderType)
     * @see net.opengis.fes20.Fes20Package#getSortPropertyType_SortOrder()
     * @model unsettable="true"
     *        extendedMetaData="kind='element' name='SortOrder' namespace='##targetNamespace'"
     * @generated
     */
    SortOrderType getSortOrder();

    /**
     * Sets the value of the '{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sort Order</em>' attribute.
     * @see net.opengis.fes20.SortOrderType
     * @see #isSetSortOrder()
     * @see #unsetSortOrder()
     * @see #getSortOrder()
     * @generated
     */
    void setSortOrder(SortOrderType value);

    /**
     * Unsets the value of the '{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetSortOrder()
     * @see #getSortOrder()
     * @see #setSortOrder(SortOrderType)
     * @generated
     */
    void unsetSortOrder();

    /**
     * Returns whether the value of the '{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Sort Order</em>' attribute is set.
     * @see #unsetSortOrder()
     * @see #getSortOrder()
     * @see #setSortOrder(SortOrderType)
     * @generated
     */
    boolean isSetSortOrder();

} // SortPropertyType
