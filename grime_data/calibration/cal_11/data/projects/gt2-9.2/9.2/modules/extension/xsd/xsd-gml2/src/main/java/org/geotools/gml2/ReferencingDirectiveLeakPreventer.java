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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml2;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaDirective;
import org.geotools.util.Utilities;

/**
 * Adapter to prevent memory leaks that occur when importing a gml schema.
 * <p>
 * When an application schema imports the gml schema a link from the gml schema to the app schema
 * is created. Since the gml schema is a singleton we store permenantly this causes a memory leak.
 * This adapter watches the {@link XSDSchema#getReferencingDirectives()} list and ensures that it 
 * does not continue to grow by only allowing unique schema references (in terms of target namespace)
 * to reference it.
 * </p>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 *
 * @source $URL$
 */
public class ReferencingDirectiveLeakPreventer implements Adapter {

    XSDSchema target;
    
    public Notifier getTarget() {
        return target;
    }

    public void setTarget(Notifier newTarget) {
        target = (XSDSchema) newTarget;
    }
    
    public boolean isAdapterForType(Object type) {
        return type instanceof XSDSchema;
    }

    public void notifyChanged(Notification notification) {
        int featureId = notification.getFeatureID(target.getClass());
        if (featureId != XSDPackage.XSD_SCHEMA__REFERENCING_DIRECTIVES) {
            return;
        }
           
        if (notification.getEventType() != Notification.ADD) {
            return;
        }
        if (!(notification.getNewValue() instanceof XSDSchemaDirective)) {
            return;
        }
        
        XSDSchemaDirective newDirective = (XSDSchemaDirective) notification.getNewValue();
        XSDSchema schema = newDirective.getSchema();
        synchronized (target) {
            ArrayList<Integer> toremove = new ArrayList();
            for (int i = 0; i < target.getReferencingDirectives().size(); i++) {
                XSDSchemaDirective directive = 
                    (XSDSchemaDirective) target.getReferencingDirectives().get(i);
                XSDSchema schema2 = directive.getSchema();
                if (schema2 == null) {
                    toremove.add(i);
                    continue;
                }
                
                String ns1 = schema != null ? schema.getTargetNamespace() : null;
                String ns2 = schema2.getTargetNamespace();
                
                if (Utilities.equals(ns1, ns2)) {
                    toremove.add(i);
                }
            }
            
            //iterate in reverse order and skip last to keep last version
            for (int i = toremove.size()-2; i > -1; i--) {
                target.getReferencingDirectives().remove(toremove.get(i).intValue());
            }
        }
    }

    

}
