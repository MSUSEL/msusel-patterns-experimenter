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

import javax.xml.namespace.QName;

import net.opengis.wcs10.AxisSubsetType;
import net.opengis.wcs10.IntervalType;
import net.opengis.wcs10.TypedLiteralType;
import net.opengis.wcs10.ValueRangeType;
import net.opengis.wcs10.Wcs10Factory;

import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.AttributeInstance;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type
 * http://www.opengis.net/wcs:RangeSubsetType_axisSubset.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;RangeSubsetType_axisSubset&quot;&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;wcs:valueEnumBaseType&quot;&gt;
 *              &lt;attribute name=&quot;name&quot; type=&quot;string&quot; use=&quot;required&quot;&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Name or identifier of one axis in this coverage. This name shall match that of an AxisDescription element in the DescribeCoverage XML response.  &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class RangeSubsetType_axisSubsetBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return WCS.RangeSubsetType_axisSubset;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return AxisSubsetType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        AxisSubsetType axis = Wcs10Factory.eINSTANCE.createAxisSubsetType();
        
        AttributeInstance[] atts = instance.getAttributes();
        for (AttributeInstance attType : atts) {
            if (attType.getName().equals("name"))
                axis.setName(attType.getText());
        }
        
        Node singleValue = node.getChild("singleValue");
        if (singleValue != null) {
            TypedLiteralType theValue = Wcs10Factory.eINSTANCE.createTypedLiteralType();
            theValue.setValue((String) singleValue.getValue());
            axis.getSingleValue().add(theValue);
        }

        Node interval = node.getChild("interval");
        if (interval != null) {
            IntervalType range = Wcs10Factory.eINSTANCE.createIntervalType();
            if (interval.getChild("min") != null) {
                TypedLiteralType theValue = Wcs10Factory.eINSTANCE.createTypedLiteralType();
                theValue.setValue((String) interval.getChildValue("min"));
                range.setMin(theValue);
            }

            if (interval.getChild("max") != null) {
                TypedLiteralType theValue = Wcs10Factory.eINSTANCE.createTypedLiteralType();
                theValue.setValue((String) interval.getChildValue("max"));
                range.setMax(theValue);
            }

            if (interval.getChild("res") != null) {
                TypedLiteralType theValue = Wcs10Factory.eINSTANCE.createTypedLiteralType();
                theValue.setValue((String) interval.getChildValue("res"));
                range.setRes(theValue);
            }

            if (interval.getAttribute("atomic") != null)
                range.setAtomic((Boolean) interval.getAttributeValue("atomic"));
            else
                range.setAtomic(false);
            
            axis.getInterval().add(range);
        }

        return axis;
    }

}
