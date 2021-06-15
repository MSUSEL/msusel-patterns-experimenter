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
 * The interpolation to be used on a specific axis 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface InterpolationAxisType extends EObject {

    /**
     * The axis name
     * 
     * @model
     */
    public String getAxis();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.InterpolationAxisType#getAxis <em>Axis</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis</em>' attribute.
     * @see #getAxis()
     * @generated
     */
    void setAxis(String value);

    /**
     * The interpolation method to be used on the specified axis
     * 
     * @model
     */
    public String getInterpolationMethod();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.InterpolationAxisType#getInterpolationMethod <em>Interpolation Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interpolation Method</em>' attribute.
     * @see #getInterpolationMethod()
     * @generated
     */
    void setInterpolationMethod(String value);
    
    

}
