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
package org.geotools.sld.bindings;

import javax.xml.namespace.QName;

import org.geotools.sld.CssParameter;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/sld:VendorOption.
 * 
 * <pre>
 * &lt;xsd:element name="VendorOption">
 *   &lt;xsd:annotation>
 *     &lt;xsd:documentation>
 *     GeoTools specific vendor extensions that allow for implementation 
 *     specific features not necessarily supported by the core SLD spec.
 *     &lt;/xsd:documentation>
 *   &lt;/xsd:annotation>
 *   &lt;xsd:complexType mixed="true">
 *     &lt;xsd:simpleContent>
 *         &lt;xsd:extension base="xsd:string">
 *            &lt;xsd:attribute name="name" type="xsd:string" />
 *         &lt;/xsd:extension>
 *     &lt;/xsd:simpleContent>
 *   &lt;/xsd:complexType>
 * &lt;/xsd:element>
 * </pre>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 *
 * @source $URL$
 */
public class SLDVendorOptionBinding extends AbstractComplexBinding {

    FilterFactory filterFactory;
    
    public SLDVendorOptionBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
    
    public QName getTarget() {
        return SLD.VENDOROPTION;
    }

    public Class getType() {
        return CssParameter.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        CssParameter option = new CssParameter((String) node.getAttributeValue("name"));
        option.setExpression(filterFactory.literal(instance.getText()));
        return option;
    }
}
