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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.Utilities;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.StyleVisitor;

/**
 * Default implementation of SelectedChannelType.
 * 
 *
 *
 * @source $URL$
 */
public class SelectedChannelTypeImpl implements SelectedChannelType {
    private FilterFactory filterFactory;

    //private Expression contrastEnhancement;
    private ContrastEnhancement contrastEnhancement;
    private String name = "channel";

    
    public SelectedChannelTypeImpl(){
        this( CommonFactoryFinder.getFilterFactory(null));
    }

    public SelectedChannelTypeImpl(FilterFactory factory) {
        filterFactory = factory;
        contrastEnhancement = contrastEnhancement(filterFactory
                .literal(1.0));
    }
    public SelectedChannelTypeImpl(FilterFactory factory, ContrastEnhancement contrast ) {
        filterFactory = factory;
        contrastEnhancement = contrast;
    }

    public SelectedChannelTypeImpl(org.opengis.style.SelectedChannelType gray) {
        filterFactory = CommonFactoryFinder.getFilterFactory2(null);
        name = gray.getChannelName();
        contrastEnhancement = new ContrastEnhancementImpl( gray.getContrastEnhancement() );        
    }

    public String getChannelName() {
        return name;
    }

    public ContrastEnhancement getContrastEnhancement() {
        return contrastEnhancement;
    }

    public void setChannelName(String name) {
        this.name = name;
    }

    public void setContrastEnhancement(org.opengis.style.ContrastEnhancement enhancement) {
        this.contrastEnhancement = ContrastEnhancementImpl.cast( enhancement );
    }

    public void setContrastEnhancement(Expression gammaValue) {
        contrastEnhancement.setGammaValue(gammaValue);
    }

    protected ContrastEnhancement contrastEnhancement(Expression expr) {
        ContrastEnhancement enhancement = new ContrastEnhancementImpl();
        enhancement.setGammaValue(filterFactory.literal(1.0));

        return enhancement;
    }

    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }

     public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }

     @Override
     public int hashCode() {
         final int PRIME = 1000003;
         int result = 0;

         if (name != null){
             result = (PRIME * result) + name.hashCode();
         }

         if (contrastEnhancement != null) {
             result = (PRIME * result) + contrastEnhancement.hashCode();
         }
         
         return result;
     }
     
     @Override
     public boolean equals(Object obj) {
     	if (this == obj) {
             return true;
         }

         if (obj instanceof SelectedChannelTypeImpl) {
        	 SelectedChannelTypeImpl other = (SelectedChannelTypeImpl) obj;

             return Utilities.equals(name, other.name)
             && Utilities.equals(contrastEnhancement, other.contrastEnhancement);
         }

         return false;
     }

}
