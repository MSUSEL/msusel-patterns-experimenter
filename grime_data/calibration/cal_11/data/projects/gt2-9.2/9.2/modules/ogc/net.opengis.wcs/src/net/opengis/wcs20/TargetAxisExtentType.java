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
 * Used in the WCS 2.0 scaling extension to specify the scale for a particular axis 
 * 
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface TargetAxisExtentType extends EObject {

    /**
     * The axis to be scaled
     * 
     * @model
     */
    public String getAxis();

  
    /**
     * Sets the value of the '{@link net.opengis.wcs20.TargetAxisExtentType#getAxis <em>Axis</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis</em>' attribute.
     * @see #getAxis()
     * @generated
     */
    void setAxis(String value);


    /**
     * The min axis value
     * @model
     */
    public double getLow();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.TargetAxisExtentType#getLow <em>Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Low</em>' attribute.
     * @see #getLow()
     * @generated
     */
    void setLow(double value);

    /**
     * The max axis value
     * @model
     */
    public double getHigh();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.TargetAxisExtentType#getHigh <em>High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>High</em>' attribute.
     * @see #getHigh()
     * @generated
     */
    void setHigh(double value);
    

}
