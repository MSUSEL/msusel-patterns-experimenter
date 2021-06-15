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
package org.geotools.filter.expression;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;

/**
 * 
 *
 * @source $URL$
 */
public class PropertyNameBuilder implements Builder<PropertyName> {
    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);    
    String xpath = null; // will result in Expression.NIL
    Name name = null;
    boolean unset = false;
    
    public PropertyNameBuilder(){
         reset();        
    }
    public PropertyNameBuilder( PropertyName propertyName ){
        reset( propertyName );        
    }
    public PropertyNameBuilder property( String xpath ){
        return name( xpath );
    }
    public PropertyNameBuilder name( String name ){
        this.xpath = name;
        this.name = null;
        unset = false;
        return this;
    }
    public PropertyNameBuilder name( Name name ){
        this.name = name;
        this.xpath = null;
        unset = false;
        return this;
    }
    public PropertyName build() {
        if( unset ){
            return null;
        }
        if( name != null ){
            return ff.property( name );
        }
        else {
            return ff.property( xpath );
        }
    }

    public PropertyNameBuilder reset() {
        unset = false;
        xpath = null;
        return this;
    }

    public PropertyNameBuilder reset( PropertyName original) {
        unset = false;
        xpath = original.getPropertyName();
        return this;
    }

    public PropertyNameBuilder unset() {
        unset = true;
        xpath = null;
        return this;
    }

}
