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
package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * The root object for wcs 2.0 scaling 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface ScalingType extends EObject {

    /**
     * Scales by uniform factor
     * 
     * @model
     */
    public ScaleByFactorType getScaleByFactor();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScalingType#getScaleByFactor <em>Scale By Factor</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale By Factor</em>' reference.
     * @see #getScaleByFactor()
     * @generated
     */
    void setScaleByFactor(ScaleByFactorType value);

    /**
     * Scales each axis by a different value
     * 
     * @model
     */
    public ScaleAxisByFactorType getScaleAxesByFactor();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScalingType#getScaleAxesByFactor <em>Scale Axes By Factor</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale Axes By Factor</em>' reference.
     * @see #getScaleAxesByFactor()
     * @generated
     */
    void setScaleAxesByFactor(ScaleAxisByFactorType value);

    /**
     * Scales each axis to a specific size
     * 
     * @model
     */
    public ScaleToSizeType getScaleToSize();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScalingType#getScaleToSize <em>Scale To Size</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale To Size</em>' reference.
     * @see #getScaleToSize()
     * @generated
     */
    void setScaleToSize(ScaleToSizeType value);

    /**
     * Scales each axis to a specific extent
     * 
     * @model
     */
    public ScaleToExtentType getScaleToExtent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ScalingType#getScaleToExtent <em>Scale To Extent</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale To Extent</em>' reference.
     * @see #getScaleToExtent()
     * @generated
     */
    void setScaleToExtent(ScaleToExtentType value);

}
