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
 * A range item, either a single value or an interval 
 *  
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface RangeItemType extends EObject {

    /**
     * The single band chosen to to be returned
     * 
     * @model
     */
    public String getRangeComponent();
    
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.RangeItemType#getRangeComponent <em>Range Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Component</em>' attribute.
     * @see #getRangeComponent()
     * @generated
     */
    void setRangeComponent(String value);


    /**
     * The band range chosen to to be returned
     * 
     * @model
     */
    public RangeIntervalType getRangeInterval();


    /**
     * Sets the value of the '{@link net.opengis.wcs20.RangeItemType#getRangeInterval <em>Range Interval</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Interval</em>' reference.
     * @see #getRangeInterval()
     * @generated
     */
    void setRangeInterval(RangeIntervalType value);
    

}
