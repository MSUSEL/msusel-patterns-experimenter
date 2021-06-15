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

import java.lang.Object;
import org.eclipse.emf.ecore.EObject;

/**
 * Contains one item from the csw:Extension container. This class does not come from the WCS model,
 * it has been created to carry around name and namespace of the various values since various
 * of them are literals 
 *  
 * @author Andrea Aime - GeoSolutions
 * @model
 */
public interface ExtensionItemType extends EObject {

    /**
     * The name of the element containing the item
     * @model
     */
    public String getName();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ExtensionItemType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * The namespace of the element 
     * @model
     */
    public String getNamespace();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ExtensionItemType#getNamespace <em>Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Namespace</em>' attribute.
     * @see #getNamespace()
     * @generated
     */
    void setNamespace(String value);

    /**
     * The content of the element, for simple elements 
     * @model
     */
    public String getSimpleContent();
    
    /**
     * Sets the value of the '{@link net.opengis.wcs20.ExtensionItemType#getSimpleContent <em>Simple Content</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Simple Content</em>' attribute.
     * @see #getSimpleContent()
     * @generated
     */
    void setSimpleContent(String value);

    /**
     * The content of the element, for simple elements 
     * @model type="java.lang.Object"
     */
    public Object getObjectContent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ExtensionItemType#getObjectContent <em>Object Content</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Object Content</em>' reference.
     * @see #getObjectContent()
     * @generated
     */
    void setObjectContent(Object value);
    
}
