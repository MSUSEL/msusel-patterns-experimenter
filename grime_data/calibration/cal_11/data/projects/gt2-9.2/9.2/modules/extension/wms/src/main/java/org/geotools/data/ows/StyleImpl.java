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
package org.geotools.data.ows;

import java.net.URL;
import java.util.List;

import org.opengis.util.InternationalString;

/**
 * 
 * @author Richard Gould
 *
 *
 *
 *
 * @source $URL$
 */
public class StyleImpl {

	private String name;
	private InternationalString title;
	private InternationalString _abstract;
	private List legendURLs;
	private URL styleSheetURL;
	private URL styleURL;
	private List featureStyles;
	private List graphicStyles;
	
	public StyleImpl() {
		
	}
	
	public StyleImpl(String name) {
		this.name = name;
	}
	
	public InternationalString getAbstract() {
		return _abstract;
	}
	public void setAbstract(InternationalString _abstract) {
		this._abstract = _abstract;
	}
	public List getFeatureStyles() {
		return featureStyles;
	}
	public void setFeatureStyles(List featureStyles) {
		this.featureStyles = featureStyles;
	}
	public List getGraphicStyles() {
		return graphicStyles;
	}
	public void setGraphicStyles(List graphicStyles) {
		this.graphicStyles = graphicStyles;
	}
	public List getLegendURLs() {
		return legendURLs;
	}
	public void setLegendURLs(List legendURLs) {
		this.legendURLs = legendURLs;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public URL getStyleSheetURL() {
		return styleSheetURL;
	}
	public void setStyleSheetURL(URL styleSheetURL) {
		this.styleSheetURL = styleSheetURL;
	}
	public URL getStyleURL() {
		return styleURL;
	}
	public void setStyleURL(URL styleURL) {
		this.styleURL = styleURL;
	}
	public InternationalString getTitle() {
		return title;
	}
	public void setTitle(InternationalString title) {
		this.title = title;
	}

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

  /**
   * Because the style's name is declared as unique identifier in the
   * interface javadocs, we will use that as our equals comparison.
   * 
   * So if two Styles have the same name, they are considered equal.
   * 
   */
    public boolean equals( Object obj ) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final StyleImpl other = (StyleImpl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
}
