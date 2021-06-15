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
 * Binding object for the type http://www.opengis.net/wcs:_GetCoverage.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="_GetCoverage"&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="sourceCoverage" type="string"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;The coverage offering (identified by its "name") that this request will draw from.&lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element name="domainSubset" type="wcs:DomainSubsetType"/&gt;
 *          &lt;element minOccurs="0" name="rangeSubset" type="wcs:RangeSubsetType"/&gt;
 *          &lt;element minOccurs="0" ref="wcs:interpolationMethod"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Spatial interpolation method to be used in  resampling data from its original form to the requested CRS and/or grid size. Method shall be among those listed for the requested coverage in the DescribeCoverage response.&lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element name="output" type="wcs:OutputType"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute fixed="WCS" name="service" type="string" use="required"/&gt;
 *      &lt;attribute fixed="1.0.0" name="version" type="string" use="required"/&gt;
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
public class _GetCoverageBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS._GetCoverage;
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
