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
package org.geotools.filter;


import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.Identifier;

/**
 * FitlerBuilder acting as a simple wrapper around an Expression.
 *
 *
 *
 * @source $URL$
 */
public class AndBuilder<P> implements Builder<And> {
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
    protected P parent;
    protected List<FilterBuilder> list;

    private List<Identifier> ids = new ArrayList<Identifier>();
    
    public AndBuilder(){
        reset();    
    }
    
    public AndBuilder( P parent){
        this.parent = parent;
        reset();
    }
    
    
    /**
     * Build an And filter
     */
    public And build() {
        if( list == null ) {
            return null;
        }
        List<Filter> filters = new ArrayList<Filter>( list.size() );
        for( FilterBuilder build : list ){
            Filter filter = build.build();
            if( filter != null ){
                filters.add( filter );
            }
        }
        if (parent == null){
            list.clear();
        }
        return ff.and( filters );
    }

    public AndBuilder<P> fid( String fid ){
        ids.add( ff.featureId(fid));
        return this;
    }
    
    public AndBuilder<P> and( Filter filter ){
        list.add( new FilterBuilder().reset( filter ));
        return this;
    }
    
    public AndBuilder<P> fid( List<String> fids ){
        for( String fid : fids ){
            ids.add( ff.featureId(fid));            
        }
        return this;
    }
    
    public P end(){
        return parent;
    }
    public AndBuilder<P> reset() {
        this.list = new ArrayList<FilterBuilder>();
        return this;
    }
    
    public AndBuilder<P> reset(And filter) {
        if( filter == null ){
            return unset();
        }
        this.list = new ArrayList<FilterBuilder>();
        if( filter.getChildren() != null ){
            for( Filter child : filter.getChildren() ){
                list.add( new FilterBuilder().reset( child ));
            }
        }
        return this;
    }

    public AndBuilder<P> unset() {       
        this.list = null;
        return this;
    }

}
