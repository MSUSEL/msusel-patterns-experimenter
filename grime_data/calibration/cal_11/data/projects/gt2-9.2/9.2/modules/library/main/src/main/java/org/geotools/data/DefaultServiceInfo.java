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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of DefaultServiceInfo as a java bean.
 * 
 * @author Jody Garnett (Refractions Research)
 *
 *
 *
 * @source $URL$
 */
public class DefaultServiceInfo implements ServiceInfo, Serializable {    
    private static final long serialVersionUID = 7975308744804800859L;
    
    protected String description;
    protected Set<String> keywords;
    protected URI publisher;
    protected URI schema;
    protected String title;
    private URI source;
    
    public DefaultServiceInfo(){                
    }
    
    public DefaultServiceInfo( ServiceInfo copy ){
        this.description = copy.getDescription();
        this.keywords = new HashSet<String>();
        if( copy.getKeywords() != null ){
            this.keywords.addAll( copy.getKeywords() );
        }
        this.publisher = copy.getPublisher();
        this.schema = copy.getSchema();
        this.title = copy.getTitle();
        this.source = copy.getSource();
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription( String description ) {
        this.description = description;
    }
    /**
     * @return the keywords
     */
    public Set<String> getKeywords() {
        return keywords;
    }
    /**
     * @param keywords the keywords to set
     */
    public void setKeywords( Set<String> keywords ) {
        this.keywords = keywords;
    }
    /**
     * @return the publisher
     */
    public URI getPublisher() {
        return publisher;
    }
    /**
     * @param publisher the publisher to set
     */
    public void setPublisher( URI publisher ) {
        this.publisher = publisher;
    }
    /**
     * @return the schema
     */
    public URI getSchema() {
        return schema;
    }
    /**
     * @param schema the schema to set
     */
    public void setSchema( URI schema ) {
        this.schema = schema;
    }
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * @return the source
     */
    public URI getSource() {
        return source;
    }
    /**
     * @param source the source to set
     */
    public void setSource( URI source ) {
        this.source = source;
    }
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ServiceInfo ");
        if( source != null ){
            buf.append( source );            
        }        
        if( this.title != null ){
            buf.append( "\n title=");
            buf.append( title );
        }
        if( this.publisher != null ){
            buf.append( "\n publisher=");
            buf.append( publisher );
        }
        if( this.publisher != null ){
            buf.append( "\n schema=");
            buf.append( schema );
        }
        if( keywords != null ){
            buf.append( "\n keywords=");
            buf.append( keywords );            
        }
        if( description != null ){
            buf.append( "\n description=");
            buf.append( description );
        }
        return buf.toString();
    }
}
