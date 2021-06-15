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
package org.geotools.csw.bindings;

import java.net.URI;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.SimpleLiteral;

import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.SimpleContentComplexEMFBinding;

public class SimpleLiteralBinding extends SimpleContentComplexEMFBinding {
    
    

    public SimpleLiteralBinding(QName target) {
        super(Csw20Factory.eINSTANCE, target);
    }
    
    @Override
    public Class getType() {
        return SimpleLiteral.class;
    }
    
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        SimpleLiteral sl = Csw20Factory.eINSTANCE.createSimpleLiteral();
        sl.setName(instance.getName());
        sl.setValue(value);
        Node scheme = node.getAttribute("scheme");
        if(scheme != null) {
            sl.setScheme((URI) scheme.getValue());
        }
        
        return sl;
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        Object result = super.getProperty(object, name);
        return result;
    }

}
