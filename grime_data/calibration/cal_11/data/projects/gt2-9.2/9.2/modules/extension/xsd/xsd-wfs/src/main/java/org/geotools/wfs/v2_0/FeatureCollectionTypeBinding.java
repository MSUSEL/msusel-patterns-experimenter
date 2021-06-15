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
package org.geotools.wfs.v2_0;

import net.opengis.wfs20.Wfs20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.bindings.WFSParsingUtils;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs/2.0:FeatureCollectionType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:complexType name="FeatureCollectionType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="wfs:SimpleFeatureCollectionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" name="AdditionalObjects" type="wfs:SimpleValueCollectionType"/&gt;
 *                  &lt;xsd:element minOccurs="0" ref="wfs:TruncatedResponse"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attributeGroup ref="wfs:StandardResponseParameters"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class FeatureCollectionTypeBinding extends AbstractComplexEMFBinding {

    public FeatureCollectionTypeBinding(Wfs20Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.FeatureCollectionType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return WFSParsingUtils.FeatureCollectionType_parse(
            (EObject) super.parse(instance, node, value), instance, node);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (!WFSParsingUtils.features((EObject) object).isEmpty()) {
            Object val = WFSParsingUtils.FeatureCollectionType_getProperty((EObject) object, name);
            if (val != null) {
                return val;
            }
        }
        
        return super.getProperty(object, name);
    }
    
    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        if ("member".equalsIgnoreCase(property)) {
            //ignore feature, handled in parse()
        }
        else {
            super.setProperty(eObject, property, value, lax);
        }
    }
}
