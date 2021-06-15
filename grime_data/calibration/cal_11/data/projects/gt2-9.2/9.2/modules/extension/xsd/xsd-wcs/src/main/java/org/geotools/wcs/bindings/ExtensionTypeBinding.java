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

import net.opengis.wcs20.ExtensionItemType;
import net.opengis.wcs20.ExtensionType;
import net.opengis.wcs20.Wcs20Factory;

import org.eclipse.emf.common.util.EList;
import org.geotools.wcs.v2_0.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Custom binding for the open ended WCS:Extension element
 * 
 * @author Andrea Aime - GeoSolutions
 *
 */
public class ExtensionTypeBinding extends AbstractComplexBinding {

    public QName getTarget() {
        return WCS.ExtensionType;
    }

    public Class getType() {
        return ExtensionType.class;
    }

    
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        
        ExtensionType et = Wcs20Factory.eINSTANCE.createExtensionType();
        
        EList<ExtensionItemType> contents = et.getContents();
        for(Object o : node.getChildren()) {
            Node child = (Node) o;
            String name = child.getComponent().getName();
            String namespace = child.getComponent().getNamespace();
            Object v = child.getValue();
            
            ExtensionItemType item = Wcs20Factory.eINSTANCE.createExtensionItemType();
            item.setName(name);
            item.setNamespace(namespace);
            if(v instanceof String) {
                item.setSimpleContent((String) v);
            } else {
                item.setObjectContent(v);
            }
            
            contents.add(item);
        }
        
        return et;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.xml.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }
}
