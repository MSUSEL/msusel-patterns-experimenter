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


import javax.swing.Icon;

import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.StyleVisitor;


/**
 * Default implementation of ExternalMark.
 * 
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class ExternalMarkImpl implements org.geotools.styling.ExternalMark {

    private OnLineResource onlineResource;
    private Icon inlineContent;
    private int index;
    private String format;

    public ExternalMarkImpl() {        
    }
    
    public ExternalMarkImpl(Icon icon) {
        this.inlineContent = icon;
        this.index = -1;
        this.onlineResource = null;
        this.format = null;
    }

    public ExternalMarkImpl(OnLineResource resource, String format, int markIndex) {
        this.inlineContent = null;
        this.index = markIndex;
        this.onlineResource = resource;
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public Icon getInlineContent() {
        return inlineContent;
    }

    public int getMarkIndex() {
        return index;
    }

    public OnLineResource getOnlineResource() {
        return onlineResource;
    }

    public Object accept(StyleVisitor visitor, Object extraData) {
        return visitor.visit( this, extraData );
    }

    public void setInlineContent(Icon inline) {
        this.inlineContent = inline;
    }

    public void getInlineContent(Icon inline) {
        setInlineContent(inline);
    }
    
    public void setFormat(String mimeType) {
        this.format = mimeType;
    }

    public void setMarkIndex(int markIndex) {
        this.index = markIndex;
    }

    public void setOnlineResource(OnLineResource resource) {
        this.onlineResource = resource;
    }
    static ExternalMarkImpl cast(org.opengis.style.ExternalMark mark) {
        if (mark == null) {
            return null;
        } else if (mark instanceof ExternalMarkImpl) {
            return (ExternalMarkImpl) mark;
        } else {
            ExternalMarkImpl copy = new ExternalMarkImpl();
            copy.setFormat( mark.getFormat() );
            copy.setMarkIndex( mark.getMarkIndex() );
            copy.setOnlineResource( mark.getOnlineResource() );
            return copy;
        }
    }
}
