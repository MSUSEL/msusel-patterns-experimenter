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
package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:intervalType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="intervalType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;An interval of values of a numeric quantity. This interval can be continuous or discrete, defined by a fixed spacing between adjacent valid values. Note that the "type" and "semantic" attributes for min/max and "res" may be different (timeInstant and duration). &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:valueRangeType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" name="res" type="wcs:TypedLiteralType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;The regular distance or spacing between the allowed values in this interval. Shall be included when the allowed values are NOT continuous in this interval. Shall not be included when the allowed values are continuous in this interval. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class IntervalTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.intervalType;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return null;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		//TODO: implement and remove call to super
		return super.parse(instance,node,value);
	}

}
