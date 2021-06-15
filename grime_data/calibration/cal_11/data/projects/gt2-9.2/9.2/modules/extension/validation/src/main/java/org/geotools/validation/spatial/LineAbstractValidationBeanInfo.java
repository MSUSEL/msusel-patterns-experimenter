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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.spatial;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ResourceBundle;

import org.geotools.validation.DefaultIntegrityValidationBeanInfo;


/**
 * LineAbstractValidationBeanInfopurpose.
 * 
 * <p>
 * Description of LineAbstractValidationBeanInfo...
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class LineAbstractValidationBeanInfo extends DefaultIntegrityValidationBeanInfo{
    /**
     * LineAbstractValidationBeanInfoconstructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public LineAbstractValidationBeanInfo(){
        super();
    }

    /**
     * Implementation of getPropertyDescriptors.
     *
     *
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
    	PropertyDescriptor[] pd2 = super.getPropertyDescriptors();
    	ResourceBundle resourceBundle = getResourceBundle(LineAbstractValidation.class);

    	if (pd2 == null) {
    		pd2 = new PropertyDescriptor[0];
    	}

    	PropertyDescriptor[] pd = new PropertyDescriptor[pd2.length + 1];
    	int i = 0;

    	for (; i < pd2.length; i++)
    		pd[i] = pd2[i];

    	try {
    		pd[i] = createPropertyDescriptor("lineTypeRef",
    				LineAbstractValidation.class, resourceBundle);
    		pd[i].setExpert(false);
    	} catch (IntrospectionException e) {
    		pd = pd2;

    		// TODO error, log here
    		e.printStackTrace();
    	}

    	return pd;
    }
}
