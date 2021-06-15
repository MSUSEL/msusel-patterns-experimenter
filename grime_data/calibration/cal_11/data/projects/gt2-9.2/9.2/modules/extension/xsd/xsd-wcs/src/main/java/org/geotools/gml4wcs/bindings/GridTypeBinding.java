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
package org.geotools.gml4wcs.bindings;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.gml.Gml4wcsFactory;
import net.opengis.gml.GridEnvelopeType;
import net.opengis.gml.GridLimitsType;
import net.opengis.gml.RectifiedGridType;

import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.gml4wcs.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/gml:GridType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;GridType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Implicitly defines an unrectified grid, which is a network composed of two or more sets of equally spaced parallel lines in which the members of each set intersect the members of the other sets at right angles. This profile does not extend AbstractGeometryType, so it defines the srsName attribute.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractGeometryType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element name=&quot;limits&quot; type=&quot;gml:GridLimitsType&quot;/&gt;
 *                  &lt;element maxOccurs=&quot;unbounded&quot; name=&quot;axisName&quot; type=&quot;string&quot;/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute name=&quot;dimension&quot; type=&quot;positiveInteger&quot; use=&quot;required&quot;/&gt;
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
public class GridTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.RectifiedGridType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        RectifiedGridType grid = Gml4wcsFactory.eINSTANCE.createRectifiedGridType();
        
        if(node.hasAttribute("srsName")) {
            grid.setSrsName(node.getAttributeValue("srsName").toString());
        }
        
        grid.setDimension((BigInteger) node.getAttribute("dimension").getValue());

        GeneralGridEnvelope limitsEnvelope = (GeneralGridEnvelope) node.getChildValue("limits");
        
//        GridLimitsType limits = Gml4wcsFactory.eINSTANCE.createGridLimitsType();
//        GridEnvelopeType gridEnelope = Gml4wcsFactory.eINSTANCE.createGridEnvelopeType();
//        List l = new ArrayList();
//             l.add(limitsEnvelope.getLow(0));
//             l.add(limitsEnvelope.getLow(1));
//        List h = new ArrayList();
//             h.add(limitsEnvelope.getHigh(0));
//             h.add(limitsEnvelope.getHigh(1));

        grid.setDimension(BigInteger.valueOf(2));
        grid.setLimits(new GridEnvelope2D(
                (int)limitsEnvelope.getLow(0), (int)limitsEnvelope.getLow(1), 
                (int)limitsEnvelope.getHigh(0), (int)limitsEnvelope.getHigh(1))
        );
        
        List<Node> axisNames = node.getChildren("axisName");
        if (axisNames != null && !axisNames.isEmpty()) {
            for (Node axisName : axisNames) {
                grid.getAxisName().add(axisName.getValue());
            }
        }

        return grid;
//       return super.parse(instance, node, value);
    }

}
